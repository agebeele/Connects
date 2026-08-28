package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.model.PrivacyLevel
import com.example.model.RoomGroup
import com.example.ui.theme.BentoBlue
import com.example.ui.theme.BentoBorderLight
import com.example.ui.theme.BentoCardBlue
import com.example.ui.theme.BentoCardLavenderGray
import com.example.ui.theme.BentoCardPurpleSubtle
import com.example.ui.theme.BentoDarkNavy
import com.example.ui.theme.BentoTextPrimary
import com.example.ui.theme.BentoTextSecondary

@Composable
fun RoomsScreen(
    rooms: List<RoomGroup>,
    onSelectRoom: (String) -> Unit,
    onLaunchCameraForRoom: (String) -> Unit,
    onGenerateMovieForRoom: (String) -> Unit,
    showCreateDialog: Boolean,
    onOpenCreateDialog: () -> Unit,
    onCloseCreateDialog: () -> Unit,
    onCreateRoom: (String, String, PrivacyLevel, Boolean) -> Unit,
    showJoinDialog: Boolean,
    onOpenJoinDialog: () -> Unit,
    onCloseJoinDialog: () -> Unit,
    onJoinRoom: (String) -> Boolean,
    inviteRoomId: String?,
    onOpenInviteDialog: (String) -> Unit,
    onCloseInviteDialog: () -> Unit,
    onInviteFriends: (String, List<String>) -> Unit,
    closeConfirmRoomId: String?,
    onOpenCloseConfirm: (String) -> Unit,
    onDismissCloseConfirm: () -> Unit,
    onConfirmCloseRoom: (String) -> Unit,
    onReopenRoom: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Header with action buttons
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Salas y Grupos de Amigos",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoTextPrimary
                            )
                        )
                        Text(
                            text = "Toma fotos y videos en grupo. Guarda recuerdos cerrados.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BentoTextSecondary
                            )
                        )
                    }
                }
            }

            // Quick Create & Join buttons in Bento Pill Style
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onOpenCreateDialog,
                        colors = ButtonDefaults.buttonColors(containerColor = BentoBlue),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("btn_open_create_room")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Nueva Sala",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    OutlinedButton(
                        onClick = onOpenJoinDialog,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = BentoTextPrimary
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("btn_open_join_room")
                    ) {
                        Icon(
                            imageVector = Icons.Default.GroupAdd,
                            contentDescription = null,
                            tint = BentoBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Unirse a Sala",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Rooms list
            items(rooms, key = { it.id }) { room ->
                RoomCardItem(
                    room = room,
                    onOpenAlbum = { onSelectRoom(room.id) },
                    onSnapMedia = { onLaunchCameraForRoom(room.id) },
                    onMakeMovie = { onGenerateMovieForRoom(room.id) },
                    onInviteFriends = { onOpenInviteDialog(room.id) },
                    onCloseRoom = { onOpenCloseConfirm(room.id) },
                    onReopenRoom = { onReopenRoom(room.id) }
                )
            }
        }

        // Create Room Dialog
        if (showCreateDialog) {
            CreateRoomDialog(
                onDismiss = onCloseCreateDialog,
                onCreate = { name, desc, priv, inviteOnly -> onCreateRoom(name, desc, priv, inviteOnly) }
            )
        }

        // Join Room Dialog
        if (showJoinDialog) {
            JoinRoomDialog(
                onDismiss = onCloseJoinDialog,
                onJoin = onJoinRoom
            )
        }

        // Invite Friends Dialog
        inviteRoomId?.let { rId ->
            val room = rooms.find { it.id == rId }
            if (room != null) {
                InviteFriendsDialog(
                    room = room,
                    onDismiss = onCloseInviteDialog,
                    onSendInvites = { friends -> onInviteFriends(room.id, friends) }
                )
            }
        }

        // Close Room Confirmation Dialog
        closeConfirmRoomId?.let { rId ->
            val room = rooms.find { it.id == rId }
            if (room != null) {
                CloseRoomConfirmDialog(
                    room = room,
                    onDismiss = onDismissCloseConfirm,
                    onConfirm = { onConfirmCloseRoom(room.id) }
                )
            }
        }
    }
}

@Composable
fun RoomCardItem(
    room: RoomGroup,
    onOpenAlbum: () -> Unit,
    onSnapMedia: () -> Unit,
    onMakeMovie: () -> Unit,
    onInviteFriends: () -> Unit,
    onCloseRoom: () -> Unit,
    onReopenRoom: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (room.isClosed) BentoCardBlue else BentoBorderLight,
                RoundedCornerShape(24.dp)
            )
            .testTag("room_card_${room.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Room Cover & Status Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(148.dp)
            ) {
                val cover = room.coverResId ?: R.drawable.photo_rooftop_party
                Image(
                    painter = painterResource(id = cover),
                    contentDescription = room.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark gradient overlay for text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.88f))
                            )
                        )
                )

                // Status tag: LIVE vs CLOSED MEMORY
                if (room.isClosed) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BentoDarkNavy.copy(alpha = 0.9f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BentoCardBlue),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = BentoCardBlue,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ÁLBUM CERRADO • RECUERDO",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else if (room.isLiveActive) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1B6C31),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (room.isInviteOnly) "EN VIVO • POR INVITACIÓN" else "ÁLBUM EN VIVO",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Privacy Badge & Join Code
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.75f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        val pIcon = when (room.defaultPrivacy) {
                            PrivacyLevel.PUBLIC -> Icons.Default.Public
                            PrivacyLevel.FRIENDS_ONLY -> Icons.Default.People
                            PrivacyLevel.PRIVATE -> Icons.Default.Lock
                        }
                        Icon(
                            imageVector = pIcon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Código: ${room.joinCode}",
                            color = BentoCardBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Room Name on cover bottom
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                ) {
                    Text(
                        text = room.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "${room.photosCount} fotos y videos • ${room.participants.size} amigos en la sala",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFE2E8F0),
                            fontSize = 11.sp
                        )
                    )
                }
            }

            // Description & Management Box
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                if (room.isClosed) {
                    // Closed Memory notification banner
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = BentoCardLavenderGray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = BentoBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Sala archivada como recuerdo. Todos los amigos pueden seguir explorando fotos, videos, comentando y reaccionando.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = BentoTextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                } else if (room.description.isNotBlank()) {
                    Text(
                        text = room.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = BentoTextSecondary
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Participants Avatar bubbles & Actions
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        room.participants.take(5).forEachIndexed { _, participant ->
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(end = 4.dp)
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color(participant.avatarColor))
                                    .border(1.dp, Color.White, CircleShape)
                            ) {
                                Text(
                                    text = participant.name.take(1).uppercase(),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Invite button bubble if active
                        if (!room.isClosed) {
                            Surface(
                                shape = CircleShape,
                                color = BentoCardBlue,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .clickable { onInviteFriends() }
                                    .testTag("btn_invite_friends_${room.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PersonAdd,
                                    contentDescription = "Invitar Amigos",
                                    tint = BentoBlue,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxSize()
                                )
                            }
                        }
                    }

                    // Action buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // AI Movie button
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = BentoCardLavenderGray,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onMakeMovie() }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Movie,
                                    contentDescription = "Película IA",
                                    tint = BentoBlue,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Película",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoBlue
                                )
                            }
                        }

                        // Open Album button
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = BentoBlue,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onOpenAlbum() }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoLibrary,
                                    contentDescription = "Ver Álbum",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Álbum",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Action Bar: Close Room or Reopen + Snap Photo/Video
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (room.isClosed) {
                        OutlinedButton(
                            onClick = onReopenRoom,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_reopen_room_${room.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.LockOpen,
                                contentDescription = null,
                                tint = BentoBlue,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Reabrir Sala",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoBlue
                            )
                        }

                        Button(
                            onClick = onOpenAlbum,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BentoCardLavenderGray),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                        ) {
                            Text(
                                text = "Ver Recuerdo",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoTextPrimary
                            )
                        }
                    } else {
                        OutlinedButton(
                            onClick = onCloseRoom,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_close_room_${room.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = BentoTextSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Cerrar Sala",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoTextSecondary
                            )
                        }

                        Button(
                            onClick = onSnapMedia,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BentoBlue),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_snap_room_${room.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Foto / Video",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InviteFriendsDialog(
    room: RoomGroup,
    onDismiss: () -> Unit,
    onSendInvites: (List<String>) -> Unit
) {
    val context = LocalContext.current
    val allFriends = listOf(
        "Sofía Mendoza",
        "Mateo Gómez",
        "Camila Torres",
        "Diego Herrera",
        "Valentina Ríos",
        "Sebastián Mora",
        "Lucía Fernández"
    )
    val selectedFriends = remember {
        mutableStateListOf<String>().apply {
            addAll(room.invitedFriends)
        }
    }
    var copiedCode by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("dialog_invite_friends")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Invitar Amigos a la Sala",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoTextPrimary
                            )
                        )
                        Text(
                            text = room.name,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BentoBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Invitation Code Bento Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = BentoCardBlue,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Column {
                            Text(
                                text = "Código de Invitación",
                                fontSize = 10.sp,
                                color = BentoTextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = room.joinCode,
                                fontSize = 16.sp,
                                color = BentoBlue,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Código de Sala", room.joinCode)
                                clipboard.setPrimaryClip(clip)
                                copiedCode = true
                                Toast.makeText(context, "Código copiado: ${room.joinCode}", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                imageVector = if (copiedCode) Icons.Default.Check else Icons.Default.ContentCopy,
                                contentDescription = "Copiar",
                                tint = BentoBlue
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Seleccionar amigos del grupo:",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BentoTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    items(allFriends) { friend ->
                        val isChecked = selectedFriends.contains(friend)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (isChecked) selectedFriends.remove(friend)
                                    else selectedFriends.add(friend)
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { checked ->
                                    if (checked) selectedFriends.add(friend)
                                    else selectedFriends.remove(friend)
                                },
                                colors = CheckboxDefaults.colors(checkedColor = BentoBlue)
                            )
                            Text(
                                text = friend,
                                color = BentoTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cerrar", color = BentoTextSecondary)
                    }

                    Button(
                        onClick = {
                            onSendInvites(selectedFriends.toList())
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BentoBlue),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_confirm_send_invites")
                    ) {
                        Text("Enviar (${selectedFriends.size})", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CloseRoomConfirmDialog(
    room: RoomGroup,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("dialog_close_room_confirm")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = BentoBlue,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "¿Cerrar '${room.name}'?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BentoTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Al cerrar la sala se convertirá en un Álbum de Recuerdo permanente. Ya no se podrán subir fotos ni videos nuevos, pero todos los amigos podrán seguir viendo el álbum completo, comentando y reaccionando a cada momento.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = BentoTextSecondary,
                        lineHeight = 18.sp
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar", color = BentoTextSecondary)
                    }

                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(containerColor = BentoDarkNavy),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_confirm_close_room")
                    ) {
                        Text("Cerrar y Guardar", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CreateRoomDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, PrivacyLevel, Boolean) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var privacy by remember { mutableStateOf(PrivacyLevel.FRIENDS_ONLY) }
    var isInviteOnly by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("dialog_create_room")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Crear Nueva Sala de Amigos",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BentoTextPrimary
                    )
                )
                Text(
                    text = "Tomen fotos y videos juntos durante la fiesta o viaje.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = BentoTextSecondary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre de la Sala (ej: Fiesta de Cumple)") },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BentoBlue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_room_name")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción del evento") },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BentoBlue
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Invite only switch Bento box
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = BentoCardBlue,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Sala por Invitación",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoTextPrimary
                                )
                            )
                            Text(
                                text = "Solo los amigos invitados podrán subir fotos y videos.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    color = BentoTextSecondary
                                )
                            )
                        }
                        Switch(
                            checked = isInviteOnly,
                            onCheckedChange = { isInviteOnly = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = BentoBlue)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

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
                        val isSelected = privacy == level
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) BentoBlue else BentoCardLavenderGray,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { privacy = level }
                        ) {
                            Text(
                                text = level.label,
                                fontSize = 11.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else BentoTextSecondary,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar", color = BentoTextSecondary)
                    }

                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onCreate(name, description, privacy, isInviteOnly)
                            }
                        },
                        enabled = name.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = BentoBlue),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_submit_create_room")
                    ) {
                        Text("Crear", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun JoinRoomDialog(
    onDismiss: () -> Unit,
    onJoin: (String) -> Boolean
) {
    var code by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("dialog_join_room")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Unirse a una Sala por Invitación",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BentoTextPrimary
                    )
                )
                Text(
                    text = "Ingresa el código que te compartió tu amigo (ej: ROOF-772 o BEACH-901).",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = BentoTextSecondary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = code,
                    onValueChange = {
                        code = it.uppercase()
                        errorMessage = null
                    },
                    label = { Text("Código de la Sala") },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BentoBlue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_join_code")
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = Color(0xFFBA1A1A),
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar", color = BentoTextSecondary)
                    }

                    Button(
                        onClick = {
                            if (code.isNotBlank()) {
                                val success = onJoin(code)
                                if (!success) {
                                    errorMessage = "Código no encontrado. Prueba con 'ROOF-772' o 'BEACH-901'."
                                }
                            }
                        },
                        enabled = code.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = BentoBlue),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_submit_join_room")
                    ) {
                        Text("Unirme", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
