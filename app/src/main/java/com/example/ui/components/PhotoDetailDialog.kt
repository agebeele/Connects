package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.R
import com.example.model.PrivacyLevel
import com.example.model.SharedPhoto
import com.example.ui.theme.BentoBlue
import com.example.ui.theme.BentoBorderLight
import com.example.ui.theme.BentoCardBlue
import com.example.ui.theme.BentoCardLavenderGray
import com.example.ui.theme.BentoCardPurpleSubtle
import com.example.ui.theme.BentoTextPrimary
import com.example.ui.theme.BentoTextSecondary

@Composable
fun PhotoDetailDialog(
    photo: SharedPhoto,
    onDismiss: () -> Unit,
    onToggleReaction: (String) -> Unit,
    onAddComment: (String) -> Unit,
    onChangePrivacy: (PrivacyLevel) -> Unit
) {
    var commentInput by remember { mutableStateOf("") }

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
                .fillMaxHeight(0.92f)
                .imePadding()
                .testTag("photo_detail_dialog")
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Modal Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(photo.authorAvatarColor))
                        ) {
                            Text(
                                text = photo.authorName.take(1).uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = photo.authorName,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoTextPrimary
                                )
                            )
                            Text(
                                text = "${photo.roomName} • ${photo.timestampFormatted}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = BentoTextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("btn_close_photo_detail")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = BentoTextSecondary
                        )
                    }
                }

                // Scrollable Content
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    // Main Image item
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .background(BentoCardLavenderGray)
                        ) {
                            if (photo.localUri != null) {
                                AsyncImage(
                                    model = photo.localUri,
                                    contentDescription = photo.caption,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                val res = photo.imageResId ?: R.drawable.photo_rooftop_party
                                Image(
                                    painter = painterResource(id = res),
                                    contentDescription = photo.caption,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }

                    // Privacy Controls Bento Card
                    item {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = "Visibilidad del Álbum:",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoTextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                PrivacyLevel.values().forEach { level ->
                                    val isSelected = photo.privacy == level
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) BentoBlue else BentoCardBlue,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { onChangePrivacy(level) }
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        ) {
                                            val pIcon = when (level) {
                                                PrivacyLevel.PUBLIC -> Icons.Default.Public
                                                PrivacyLevel.FRIENDS_ONLY -> Icons.Default.People
                                                PrivacyLevel.PRIVATE -> Icons.Default.Lock
                                            }
                                            Icon(
                                                imageVector = pIcon,
                                                contentDescription = level.label,
                                                tint = if (isSelected) Color.White else BentoBlue,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = level.label,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) Color.White else BentoBlue
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Quick Reactions row
                    item {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            listOf("❤️", "🔥", "😂", "🚀", "📸", "✨").forEach { emoji ->
                                val existing = photo.reactions.find { it.emoji == emoji }
                                val reacted = existing?.userReacted ?: false
                                val count = existing?.count ?: 0

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (reacted) BentoCardBlue else BentoCardLavenderGray,
                                    border = if (reacted) androidx.compose.foundation.BorderStroke(1.5.dp, BentoBlue) else null,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable { onToggleReaction(emoji) }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(text = emoji, fontSize = 16.sp)
                                        if (count > 0) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "$count",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = if (reacted) BentoBlue else BentoTextSecondary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Caption
                    if (photo.caption.isNotBlank()) {
                        item {
                            Text(
                                text = photo.caption,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = BentoTextPrimary
                                ),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }

                    // Comments Title
                    item {
                        Text(
                            text = "Comentarios en tiempo real (${photo.comments.size})",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoTextPrimary
                            ),
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 4.dp)
                        )
                    }

                    // Comments List
                    if (photo.comments.isEmpty()) {
                        item {
                            Text(
                                text = "Aún no hay comentarios. ¡Sé el primero en comentar a tus amigos!",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = BentoTextSecondary
                                ),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                            )
                        }
                    } else {
                        items(photo.comments) { comment ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(Color(comment.authorAvatarColor))
                                    ) {
                                    Text(
                                        text = comment.authorName.take(1).uppercase(),
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            BentoCardLavenderGray,
                                            RoundedCornerShape(14.dp)
                                        )
                                        .padding(10.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = comment.authorName,
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = BentoTextPrimary
                                            )
                                        )
                                        Text(
                                            text = comment.timestampFormatted,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 10.sp,
                                                color = BentoTextSecondary
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = comment.text,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = BentoTextPrimary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Comment input box
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    OutlinedTextField(
                        value = commentInput,
                        onValueChange = { commentInput = it },
                        placeholder = { Text("Escribe un comentario a los amigos...", color = BentoTextSecondary) },
                        maxLines = 2,
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = BentoTextPrimary,
                            unfocusedTextColor = BentoTextPrimary,
                            focusedBorderColor = BentoBlue,
                            unfocusedBorderColor = BentoBorderLight
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_comment_text")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (commentInput.isNotBlank()) {
                                onAddComment(commentInput)
                                commentInput = ""
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(BentoBlue)
                            .testTag("btn_send_comment")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Enviar",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
