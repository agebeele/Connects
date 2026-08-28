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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.PrivacyLevel
import com.example.model.RoomGroup
import com.example.model.SharedPhoto
import com.example.ui.theme.BentoBlue
import com.example.ui.theme.BentoBorderLight
import com.example.ui.theme.BentoCardBlue
import com.example.ui.theme.BentoCardLavenderGray
import com.example.ui.theme.BentoCardPurpleSubtle
import com.example.ui.theme.BentoDarkNavy
import com.example.ui.theme.BentoTextPrimary
import com.example.ui.theme.BentoTextSecondary

@Composable
fun ProfileScreen(
    rooms: List<RoomGroup>,
    photos: List<SharedPhoto>,
    cloudBackupCount: Int,
    onOpenDesktopSync: () -> Unit,
    onPhotoClick: (SharedPhoto) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        // User Profile Header Bento Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        BentoBorderLight,
                        RoundedCornerShape(24.dp)
                    )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Avatar with Bento Blue Accent
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(BentoCardBlue)
                            .border(2.dp, BentoBlue, CircleShape)
                    ) {
                        Text(
                            text = "A",
                            color = BentoBlue,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Alex Rivera",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoTextPrimary
                        )
                    )

                    Text(
                        text = "@alex_connects • Amigos & Fotografía",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = BentoTextSecondary
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats Bento Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = BentoCardBlue,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(vertical = 12.dp)
                            ) {
                                Text(
                                    text = "${rooms.size}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BentoBlue
                                    )
                                )
                                Text(
                                    text = "Salas Activas",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = BentoTextSecondary,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = BentoCardLavenderGray,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(vertical = 12.dp)
                            ) {
                                Text(
                                    text = "${photos.size}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BentoTextPrimary
                                    )
                                )
                                Text(
                                    text = "Fotos Álbum",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = BentoTextSecondary,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = BentoCardPurpleSubtle,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(vertical = 12.dp)
                            ) {
                                Text(
                                    text = "$cloudBackupCount",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1B6C31)
                                    )
                                )
                                Text(
                                    text = "Nube Synced",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = BentoTextSecondary,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Multiplatform & Cloud Backup Status Bento Box
        item {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = BentoCardPurpleSubtle,
                border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onOpenDesktopSync() }
                    .testTag("btn_profile_desktop_sync")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Devices,
                            contentDescription = "Multiplataforma",
                            tint = BentoBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Acceso Multiplataforma & Nube",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoTextPrimary
                            )
                        )
                        Text(
                            text = "Conecta con PC, Mac o Web con QR. Copia segura activa.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BentoTextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = "Ver QR",
                        tint = BentoBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Shared Albums Published to My Profile
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Álbumes Compartidos en mi Perfil",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BentoTextPrimary
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "Tus amigos de cada sala pueden ver, reaccionar y comentar estas fotos.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = BentoTextSecondary
                    )
                )
            }
        }

        // List of Shared Albums published to profile
        items(rooms) { room ->
            val roomPhotos = photos.filter { it.roomId == room.id }
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = room.name,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoTextPrimary
                                )
                            )
                            Text(
                                text = "${roomPhotos.size} fotos compartidas con ${room.participants.size} amigos",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = BentoTextSecondary
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = BentoCardBlue
                        ) {
                            Text(
                                text = room.defaultPrivacy.label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoBlue,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Photo Thumbnails Row
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(roomPhotos) { photo ->
                            val res = photo.imageResId ?: R.drawable.photo_rooftop_party
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { onPhotoClick(photo) }
                            ) {
                                Image(
                                    painter = painterResource(id = res),
                                    contentDescription = photo.caption,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                // Reactions bubble overlay
                                if (photo.reactions.isNotEmpty()) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color.Black.copy(alpha = 0.6f),
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(4.dp)
                                    ) {
                                        Text(
                                            text = "${photo.reactions.sumOf { it.count }}❤️",
                                            fontSize = 9.sp,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
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

