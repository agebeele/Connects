package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.PrivacyLevel
import com.example.model.RoomGroup
import com.example.model.SharedPhoto
import com.example.ui.components.PhotoCard
import com.example.ui.theme.BentoBlue
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoBorderLight
import com.example.ui.theme.BentoCardBlue
import com.example.ui.theme.BentoCardDark
import com.example.ui.theme.BentoCardLavenderGray
import com.example.ui.theme.BentoCardPurpleSubtle
import com.example.ui.theme.BentoDarkNavy
import com.example.ui.theme.BentoPurpleChip
import com.example.ui.theme.BentoPurpleIcon
import com.example.ui.theme.BentoTextMuted
import com.example.ui.theme.BentoTextPrimary
import com.example.ui.theme.BentoTextSecondary

@Composable
fun FeedScreen(
    rooms: List<RoomGroup>,
    photos: List<SharedPhoto>,
    selectedRoomId: String?,
    cloudBackupCount: Int = 24,
    onSelectRoom: (String?) -> Unit,
    onPhotoClick: (SharedPhoto) -> Unit,
    onToggleReaction: (String, String) -> Unit,
    onOpenComments: (SharedPhoto) -> Unit,
    onChangePrivacy: (String, PrivacyLevel) -> Unit,
    onCreateRoom: () -> Unit,
    onLaunchCamera: () -> Unit,
    onOpenMovieStudio: () -> Unit = {},
    onOpenDesktopSync: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val activeRoom = rooms.find { it.id == selectedRoomId } ?: rooms.firstOrNull()

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 96.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Bento Grid Hub Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Bento Card 1: Live Room Group Album Hero (Col-span 2)
                    Card(
                        shape = RoundedCornerShape(26.dp),
                        colors = CardDefaults.cardColors(containerColor = BentoCardBlue),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(26.dp))
                            .clickable { onSelectRoom(activeRoom?.id) }
                            .testTag("bento_card_live_room")
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            // Subtle Watermark Icon in background
                            Icon(
                                imageVector = Icons.Default.PhotoLibrary,
                                contentDescription = null,
                                tint = BentoBlue.copy(alpha = 0.12f),
                                modifier = Modifier
                                    .size(90.dp)
                                    .align(Alignment.BottomEnd)
                            )

                            Column(modifier = Modifier.fillMaxWidth()) {
                                // Top Row: Badge & Room Name
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(20.dp),
                                        color = BentoBlue
                                    ) {
                                        Text(
                                            text = "LIVE ROOM",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.5.sp,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }

                                    Text(
                                        text = activeRoom?.name ?: "Amigos & Viajes",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = BentoDarkNavy
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Main Callout
                                Text(
                                    text = "${photos.size} new photos added\nto the group album",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BentoDarkNavy,
                                        lineHeight = 26.sp,
                                        fontSize = 20.sp
                                    )
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Stacked Avatar Circles & Participants
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(modifier = Modifier.height(34.dp)) {
                                        val avatarColors = listOf(0xFF0061A4, 0xFF6366F1, 0xFFBA1A1A, 0xFF00884A)
                                        avatarColors.forEachIndexed { index, colorVal ->
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier
                                                    .padding(start = (index * 22).dp)
                                                    .size(34.dp)
                                                    .clip(CircleShape)
                                                    .border(2.dp, BentoCardBlue, CircleShape)
                                                    .background(Color(colorVal))
                                            ) {
                                                Text(
                                                    text = when (index) {
                                                        0 -> "A"
                                                        1 -> "S"
                                                        2 -> "C"
                                                        else -> "+${activeRoom?.participants?.size ?: 4}"
                                                    },
                                                    color = Color.White,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(96.dp))

                                    Text(
                                        text = "${activeRoom?.participants?.size ?: 6} amigos activos",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = BentoDarkNavy.copy(alpha = 0.8f),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Bento Row: 2 Asymmetric Feature Cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Bento Card 2 (Col 1): AI Movie Studio Highlights
                        Card(
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = BentoCardLavenderGray),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(160.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .clickable { onOpenMovieStudio() }
                                .testTag("bento_card_ai_movie")
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MovieFilter,
                                        contentDescription = "AI Movie",
                                        tint = BentoTextSecondary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = BentoBorderLight
                                    ) {
                                        Text(
                                            text = "AI MAGIC",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BentoTextSecondary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "Your Weekend\nHighlights Movie",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BentoTextPrimary,
                                        lineHeight = 18.sp
                                    )
                                )

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.padding(vertical = 6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayCircle,
                                            contentDescription = "Ver",
                                            tint = BentoBlue,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Ver Película",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BentoBlue
                                        )
                                    }
                                }
                            }
                        }

                        // Bento Card 3 (Col 2): Cloud Sync & Multiplatform
                        Card(
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = BentoCardPurpleSubtle),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(160.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .border(1.dp, BentoBorder, RoundedCornerShape(24.dp))
                                .clickable { onOpenDesktopSync() }
                                .testTag("bento_card_cloud_sync")
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(BentoPurpleChip)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CloudDone,
                                            contentDescription = "Cloud Synced",
                                            tint = BentoPurpleIcon,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = BentoPurpleChip
                                    ) {
                                        Text(
                                            text = "SYNCED",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BentoPurpleIcon,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "$cloudBackupCount Fotos Respaldadas\nen la Nube",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BentoTextPrimary,
                                        lineHeight = 16.sp
                                    )
                                )

                                Text(
                                    text = "2.4 GB Cifrado y Multiplataforma",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = BentoTextMuted,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }

                    // Bento Card 4: Quick Access Action Card (Dark Bento)
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = BentoCardDark),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .clickable { onLaunchCamera() }
                            .testTag("bento_card_quick_access")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "QUICK ACCESS",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Start New Room / Snap Photo",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 16.sp
                                    )
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = BentoBlue,
                                modifier = Modifier.size(46.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PhotoCamera,
                                        contentDescription = "Cámara",
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Live Salas / Stories Row
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp)
                ) {
                    Text(
                        text = "Salas Activas de Amigos",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoTextPrimary
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // "Todas las salas" story circle
                        item {
                            val isAllSelected = selectedRoomId == null
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onSelectRoom(null) }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(if (isAllSelected) BentoCardBlue else BentoCardLavenderGray)
                                        .border(
                                            width = if (isAllSelected) 2.dp else 1.dp,
                                            color = if (isAllSelected) BentoBlue else BentoBorderLight,
                                            shape = CircleShape
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PhotoLibrary,
                                        contentDescription = "Todos",
                                        tint = if (isAllSelected) BentoBlue else BentoTextSecondary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Todos",
                                    fontSize = 11.sp,
                                    fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isAllSelected) BentoBlue else BentoTextSecondary
                                )
                            }
                        }

                        // Each active room item
                        items(rooms) { room ->
                            val isSelected = selectedRoomId == room.id
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .width(68.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onSelectRoom(room.id) }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) BentoBlue else BentoBorderLight,
                                            shape = CircleShape
                                        )
                                        .padding(if (isSelected) 2.dp else 0.dp)
                                ) {
                                    val cover = room.coverResId ?: R.drawable.photo_rooftop_party
                                    Image(
                                        painter = painterResource(id = cover),
                                        contentDescription = room.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                    )

                                    if (room.isLiveActive) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .size(12.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF1B6C31))
                                                .border(1.5.dp, Color.White, CircleShape)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = room.name,
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) BentoBlue else BentoTextSecondary
                                )
                            }
                        }

                        // Create room button
                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onCreateRoom() }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(BentoCardLavenderGray)
                                        .border(1.dp, BentoBorderLight, CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Crear Sala",
                                        tint = BentoTextPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Crear Sala",
                                    fontSize = 11.sp,
                                    color = BentoTextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Room Filter Banner if room selected
            if (selectedRoomId != null) {
                val currentRoom = rooms.find { it.id == selectedRoomId }
                if (currentRoom != null) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = if (currentRoom.isClosed) BentoCardLavenderGray else BentoCardBlue.copy(alpha = 0.5f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (currentRoom.isClosed) "Álbum Cerrado: ${currentRoom.name}" else "Álbum Compartido de ${currentRoom.name}",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = BentoDarkNavy
                                        )
                                    )
                                    Text(
                                        text = if (currentRoom.isClosed) "Guardado como recuerdo • Comenta y reacciona a los momentos" else "${currentRoom.participants.size} participantes • Código: ${currentRoom.joinCode}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = BentoTextSecondary,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = BentoBlue,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { onSelectRoom(null) }
                                ) {
                                    Text(
                                        text = "Ver Todos",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Shared Photos Feed
            if (photos.isEmpty()) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = BentoTextSecondary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Aún no hay fotos en este álbum",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "¡Abre la cámara y toma la primera foto para que todos tus amigos la vean en vivo!",
                            style = MaterialTheme.typography.bodySmall.copy(
                                textAlign = TextAlign.Center,
                                color = BentoTextSecondary
                            )
                        )
                    }
                }
            } else {
                items(photos, key = { it.id }) { photo ->
                    PhotoCard(
                        photo = photo,
                        onPhotoClick = { onPhotoClick(photo) },
                        onToggleReaction = { emoji -> onToggleReaction(photo.id, emoji) },
                        onOpenComments = { onOpenComments(photo) },
                        onChangePrivacy = { newPrivacy -> onChangePrivacy(photo.id, newPrivacy) }
                    )
                }
            }
        }

        // Quick Snap Floating Action Button
        FloatingActionButton(
            onClick = onLaunchCamera,
            containerColor = BentoBlue,
            contentColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 96.dp, end = 20.dp)
                .testTag("fab_quick_camera")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Tomar Foto",
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Foto a Sala",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

