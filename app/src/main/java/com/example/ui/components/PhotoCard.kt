package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.model.CloudBackupState
import com.example.model.PrivacyLevel
import com.example.model.SharedPhoto
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.BentoBlue
import com.example.ui.theme.BentoBorderLight
import com.example.ui.theme.BentoCardBlue
import com.example.ui.theme.BentoCardLavenderGray
import com.example.ui.theme.BentoDarkNavy
import com.example.ui.theme.BentoTextPrimary
import com.example.ui.theme.BentoTextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PhotoCard(
    photo: SharedPhoto,
    onPhotoClick: () -> Unit,
    onToggleReaction: (String) -> Unit,
    onOpenComments: () -> Unit,
    onChangePrivacy: (PrivacyLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(
                1.dp,
                BentoBorderLight,
                RoundedCornerShape(24.dp)
            )
            .testTag("photo_card_${photo.id}")
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Room Header & Author Meta
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Author Avatar Initial
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(photo.authorAvatarColor))
                    ) {
                        Text(
                            text = photo.authorName.take(1).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = photo.authorName,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoTextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            // Privacy Tag Badge
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = BentoCardLavenderGray,
                                modifier = Modifier.padding(2.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                ) {
                                    val privIcon = when (photo.privacy) {
                                        PrivacyLevel.PUBLIC -> Icons.Default.Public
                                        PrivacyLevel.FRIENDS_ONLY -> Icons.Default.People
                                        PrivacyLevel.PRIVATE -> Icons.Default.Lock
                                    }
                                    Icon(
                                        imageVector = privIcon,
                                        contentDescription = photo.privacy.label,
                                        tint = BentoTextSecondary,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = photo.privacy.label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = BentoTextSecondary
                                        )
                                    )
                                }
                            }
                        }

                        // Room Name and timestamp
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "en ",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = BentoTextSecondary,
                                    fontSize = 12.sp
                                )
                            )
                            Text(
                                text = photo.roomName,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = BentoBlue,
                                    fontSize = 12.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = " • ${photo.timestampFormatted}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = BentoTextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                // Options Menu (Privacy toggle, Backup status)
                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Opciones",
                            tint = BentoTextSecondary
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Visibilidad: Público") },
                            onClick = {
                                onChangePrivacy(PrivacyLevel.PUBLIC)
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Public, contentDescription = null)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Visibilidad: Solo Amigos") },
                            onClick = {
                                onChangePrivacy(PrivacyLevel.FRIENDS_ONLY)
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.People, contentDescription = null)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Visibilidad: Privado (Solo Sala)") },
                            onClick = {
                                onChangePrivacy(PrivacyLevel.PRIVATE)
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null)
                            }
                        )
                    }
                }
            }

            // Photo Viewer Box with Badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .background(Color(0xFF1E293B))
                    .clickable { onPhotoClick() }
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

                // AI Highlight Badge
                if (photo.isAiHighlightSuggested) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Black.copy(alpha = 0.7f),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Sugerencia IA",
                                tint = AmberGlow,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Destacado IA",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Cloud Backup State Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        val isSynced = photo.backupState == CloudBackupState.SYNCED
                        Icon(
                            imageVector = if (isSynced) Icons.Default.CloudDone else Icons.Default.CloudUpload,
                            contentDescription = "Cloud backup",
                            tint = if (isSynced) Color(0xFF00E676) else AmberGlow,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isSynced) "Cloud Sync" else "Subiendo...",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Filter Tag Pill at bottom-right of image
                if (photo.filterName != "Normal") {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.6f),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Filtro: ${photo.filterName}",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                // Video Badge & Play Button Overlay
                if (photo.isVideo) {
                    // Center play icon
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.6f))
                            .border(1.5.dp, Color.White, CircleShape)
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.PlayArrow,
                            contentDescription = "Reproducir Video",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Video duration badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BentoDarkNavy.copy(alpha = 0.85f),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.Videocam,
                                contentDescription = null,
                                tint = BentoCardBlue,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            val dur = if (photo.videoDurationSeconds > 0) photo.videoDurationSeconds else 15
                            Text(
                                text = "0:${dur.toString().padStart(2, '0')}",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Caption
            if (photo.caption.isNotBlank()) {
                Text(
                    text = photo.caption,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = BentoTextPrimary,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 20.sp
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }

            // Interactive Reactions & Comments Action Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            ) {
                // Real-time Reaction Chips
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    val defaultEmojis = listOf("❤️", "🔥", "✨", "📸")
                    defaultEmojis.forEach { emoji ->
                        val existing = photo.reactions.find { it.emoji == emoji }
                        val count = existing?.count ?: 0
                        val reacted = existing?.userReacted ?: false

                        val chipBg by animateColorAsState(
                            targetValue = if (reacted) BentoCardBlue else BentoCardLavenderGray.copy(alpha = 0.6f),
                            animationSpec = spring(),
                            label = "chip_bg"
                        )
                        val chipBorder = if (reacted) BentoBlue else Color.Transparent

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = chipBg,
                            border = if (reacted) androidx.compose.foundation.BorderStroke(1.dp, chipBorder) else null,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { onToggleReaction(emoji) }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = emoji,
                                    fontSize = 14.sp
                                )
                                if (count > 0) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "$count",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (reacted) FontWeight.Bold else FontWeight.Medium,
                                            color = if (reacted) BentoDarkNavy else BentoTextSecondary,
                                            fontSize = 12.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Comments launcher button
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = BentoCardLavenderGray.copy(alpha = 0.6f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onOpenComments() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = "Comentarios",
                            tint = BentoTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${photo.comments.size}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = BentoTextSecondary,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

