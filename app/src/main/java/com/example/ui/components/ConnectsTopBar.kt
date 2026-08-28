package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import com.example.ui.theme.BentoBlue
import com.example.ui.theme.BentoCanvasBg
import com.example.ui.theme.BentoCardBlue
import com.example.ui.theme.BentoDarkNavy
import com.example.ui.theme.BentoTextMuted
import com.example.ui.theme.BentoTextSecondary
import com.example.ui.theme.SunsetPink

@Composable
fun ConnectsTopBar(
    unreadNotificationsCount: Int,
    isCloudSyncing: Boolean,
    onOpenNotifications: () -> Unit,
    onOpenDesktopSync: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 12.dp)
        ) {
            // Bento Brand & Friends Active Status
            Column {
                Text(
                    text = "Connects",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.5).sp,
                        color = BentoBlue,
                        fontSize = 24.sp
                    )
                )
                Text(
                    text = "4 friends active now",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = BentoTextSecondary,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )
                )
            }

            // Quick Bento Action Badges
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Notifications Bento Action Circle
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(BentoCardBlue)
                        .clickable { onOpenNotifications() }
                        .testTag("btn_notifications")
                ) {
                    BadgedBox(
                        badge = {
                            if (unreadNotificationsCount > 0) {
                                Badge(
                                    containerColor = SunsetPink,
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = "$unreadNotificationsCount",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificaciones",
                            tint = BentoDarkNavy,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                // Profile Avatar / Cloud Sync status ring
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .border(2.dp, BentoBlue, CircleShape)
                        .padding(2.dp)
                        .clickable { onOpenDesktopSync() }
                        .testTag("btn_desktop_sync")
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BentoBlue)
                    ) {
                        Text(
                            text = "A",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    if (isCloudSyncing) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(BentoBlue)
                                .border(1.5.dp, Color.White, CircleShape)
                                .align(Alignment.BottomEnd)
                        )
                    }
                }
            }
        }
    }
}

