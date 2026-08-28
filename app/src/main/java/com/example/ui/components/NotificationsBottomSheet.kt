package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.NotificationItem
import com.example.model.NotificationType
import com.example.ui.theme.BentoBlue
import com.example.ui.theme.BentoBorderLight
import com.example.ui.theme.BentoCardBlue
import com.example.ui.theme.BentoCardLavenderGray
import com.example.ui.theme.BentoCardPurpleSubtle
import com.example.ui.theme.BentoTextPrimary
import com.example.ui.theme.BentoTextSecondary

@Composable
fun NotificationsBottomSheet(
    notifications: List<NotificationItem>,
    onDismiss: () -> Unit,
    onNotificationClick: (NotificationItem) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .testTag("notifications_sheet")
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BentoCardBlue)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificaciones",
                                tint = BentoBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Notificaciones en Vivo",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoTextPrimary
                            )
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = BentoTextSecondary
                        )
                    }
                }

                Divider(color = BentoBorderLight)

                if (notifications.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        Text(
                            text = "No tienes notificaciones recientes. ¡Cuando tus amigos suban fotos a la sala te avisaremos al instante!",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = BentoTextSecondary
                            )
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(notifications) { notif ->
                            val icon = when (notif.type) {
                                NotificationType.NEW_PHOTO -> Icons.Default.PhotoCamera
                                NotificationType.NEW_REACTION -> Icons.Default.Favorite
                                NotificationType.NEW_COMMENT -> Icons.Default.Notifications
                                NotificationType.AI_HIGHLIGHT_READY -> Icons.Default.Movie
                                NotificationType.CLOUD_BACKUP -> Icons.Default.CloudDone
                                NotificationType.ROOM_INVITE -> Icons.Default.PhotoCamera
                            }
                            val iconTint = when (notif.type) {
                                NotificationType.NEW_PHOTO -> BentoBlue
                                NotificationType.NEW_REACTION -> Color(0xFFE11D48)
                                NotificationType.AI_HIGHLIGHT_READY -> BentoBlue
                                else -> Color(0xFF6366F1)
                            }
                            val containerBg = when (notif.type) {
                                NotificationType.NEW_PHOTO -> BentoCardBlue
                                NotificationType.NEW_REACTION -> BentoCardPurpleSubtle
                                else -> BentoCardLavenderGray
                            }

                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = containerBg,
                                border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .clickable { onNotificationClick(notif) }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(Color.White)
                                    ) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = null,
                                            tint = iconTint,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = notif.title,
                                                style = MaterialTheme.typography.labelLarge.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = BentoTextPrimary
                                                )
                                            )
                                            Text(
                                                text = notif.timestampFormatted,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = BentoTextSecondary,
                                                    fontSize = 11.sp
                                                )
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = notif.message,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = BentoTextSecondary
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
