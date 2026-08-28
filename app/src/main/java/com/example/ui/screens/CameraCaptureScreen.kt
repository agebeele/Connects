package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.BentoBlue
import com.example.ui.theme.BentoBorderLight
import com.example.ui.theme.BentoCardBlue
import com.example.ui.theme.BentoCardLavenderGray
import com.example.ui.theme.BentoDarkNavy
import com.example.ui.theme.BentoTextPrimary
import com.example.ui.theme.BentoTextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CameraCaptureScreen(
    rooms: List<RoomGroup>,
    selectedRoomId: String,
    filter: String,
    privacy: PrivacyLevel,
    caption: String,
    isFlashEnabled: Boolean,
    isVideoMode: Boolean = false,
    onSelectRoom: (String) -> Unit,
    onSelectFilter: (String) -> Unit,
    onSelectPrivacy: (PrivacyLevel) -> Unit,
    onCaptionChange: (String) -> Unit,
    onToggleFlash: () -> Unit,
    onToggleVideoMode: () -> Unit = {},
    onCapture: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var isFrontCamera by remember { mutableStateOf(false) }
    var showGrid by remember { mutableStateOf(true) }
    var isFlashing by remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }
    var showRoomDropdown by remember { mutableStateOf(false) }
    var selectedSamplePhotoRes by remember { mutableStateOf<Int?>(R.drawable.photo_rooftop_party) }

    val activeRoom = rooms.find { it.id == selectedRoomId } ?: rooms.firstOrNull()

    val availableFilters = listOf(
        "Normal",
        "Cyberpunk",
        "Film 35mm",
        "Golden Hour",
        "Vivid Warm",
        "B&W Moody",
        "Vintage Sunset"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(bottom = 80.dp)
    ) {
        // Main Camera Viewfinder Frame
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(28.dp))
                .background(Color(0xFF0F172A))
        ) {
            // Viewfinder image simulation
            val currentRes = selectedSamplePhotoRes ?: R.drawable.photo_rooftop_party
            Image(
                painter = painterResource(id = currentRes),
                contentDescription = "Camera Viewfinder",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(if (isFrontCamera) -1f else 1f, 1f)
            )

            // Dynamic filter color tint overlay
            val filterOverlay = when (filter) {
                "Cyberpunk" -> Brush.verticalGradient(listOf(Color(0xFF00E5FF).copy(alpha = 0.25f), Color(0xFFFF3366).copy(alpha = 0.25f)))
                "Golden Hour" -> Brush.verticalGradient(listOf(Color(0xFFFFAB00).copy(alpha = 0.3f), Color(0xFFFF3366).copy(alpha = 0.2f)))
                "Film 35mm" -> Brush.verticalGradient(listOf(Color(0xFF8D6E63).copy(alpha = 0.3f), Color.Transparent))
                "Vivid Warm" -> Brush.verticalGradient(listOf(Color(0xFFFF7043).copy(alpha = 0.25f), Color.Transparent))
                "B&W Moody" -> Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.45f), Color.Black.copy(alpha = 0.45f)))
                "Vintage Sunset" -> Brush.verticalGradient(listOf(Color(0xFFFF3366).copy(alpha = 0.25f), Color(0xFFFFAB00).copy(alpha = 0.25f)))
                else -> Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(filterOverlay)
            )

            // Viewfinder Rule-of-Thirds Grid
            if (showGrid) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.2f)))
                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.2f)))
                    Spacer(modifier = Modifier.weight(1f))
                }
                Row(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.fillMaxHeight().width(1.dp).background(Color.White.copy(alpha = 0.2f)))
                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.fillMaxHeight().width(1.dp).background(Color.White.copy(alpha = 0.2f)))
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            // Flash Animation Flashbang
            if (isFlashing) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                )
            }

            // Top Camera Bar: Linked Sala Selector & Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Flash toggle
                IconButton(
                    onClick = onToggleFlash,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = if (isFlashEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                        contentDescription = "Flash",
                        tint = if (isFlashEnabled) AmberGlow else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Linked Sala Selector Pill
                Box {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Black.copy(alpha = 0.75f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BentoBlue.copy(alpha = 0.8f)),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { showRoomDropdown = true }
                            .testTag("btn_select_camera_room")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (activeRoom?.isClosed == true) Color(0xFFBA1A1A) else Color(0xFF00E676))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Sala: ${activeRoom?.name ?: "Elegir Sala"}",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showRoomDropdown,
                        onDismissRequest = { showRoomDropdown = false }
                    ) {
                        rooms.forEach { room ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(room.name)
                                        if (room.isClosed) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("(Cerrada)", fontSize = 11.sp, color = Color(0xFFBA1A1A))
                                        }
                                    }
                                },
                                onClick = {
                                    onSelectRoom(room.id)
                                    showRoomDropdown = false
                                }
                            )
                        }
                    }
                }

                // Flip camera button
                IconButton(
                    onClick = { isFrontCamera = !isFrontCamera },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Cameraswitch,
                        contentDescription = "Girar cámara",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Closed room warning pill if room is closed
            if (activeRoom?.isClosed == true) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = BentoDarkNavy.copy(alpha = 0.9f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BentoCardBlue),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 58.dp, start = 20.dp, end = 20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = BentoCardBlue,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Sala cerrada como recuerdo. Selecciona otra sala para subir contenido.",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                // Center Scene Selector (Switch sample view finder scenarios)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 58.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.Black.copy(alpha = 0.6f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val scenes = listOf(
                                "Fiesta Terraza" to R.drawable.photo_rooftop_party,
                                "Playa Atardecer" to R.drawable.photo_beach_sunset,
                                "Roadtrip Montaña" to R.drawable.photo_mountain_roadtrip
                            )
                            scenes.forEach { (label, res) ->
                                val isSelected = selectedSamplePhotoRes == res
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) BentoBlue else Color.Transparent,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { selectedSamplePhotoRes = res }
                                ) {
                                    Text(
                                        text = label,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom Controls Overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.95f))
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Caption input
            OutlinedTextField(
                value = caption,
                onValueChange = onCaptionChange,
                placeholder = {
                    Text(
                        text = if (isVideoMode) "Escribe una descripción para el video grupal..." else "Escribe una frase para el álbum de los amigos...",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Black.copy(alpha = 0.6f),
                    unfocusedContainerColor = Color.Black.copy(alpha = 0.4f),
                    focusedBorderColor = BentoBlue,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_camera_caption")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Privacy Selector Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                PrivacyLevel.values().forEach { level ->
                    val isSelected = privacy == level
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) BentoBlue else Color.Black.copy(alpha = 0.6f),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, Color.White) else null,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectPrivacy(level) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(vertical = 6.dp)
                        ) {
                            val pIcon = when (level) {
                                PrivacyLevel.PUBLIC -> Icons.Default.Public
                                PrivacyLevel.FRIENDS_ONLY -> Icons.Default.People
                                PrivacyLevel.PRIVATE -> Icons.Default.Lock
                            }
                            Icon(
                                imageVector = pIcon,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = level.label,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Carousel
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(availableFilters) { fName ->
                    val isSelected = filter == fName
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) BentoCardBlue else Color.White.copy(alpha = 0.15f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onSelectFilter(fName) }
                    ) {
                        Text(
                            text = fName,
                            color = if (isSelected) BentoDarkNavy else Color.White,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Mode Selector: FOTO vs VIDEO
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "FOTO",
                    color = if (!isVideoMode) BentoCardBlue else Color.White.copy(alpha = 0.6f),
                    fontWeight = if (!isVideoMode) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { if (isVideoMode) onToggleVideoMode() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("btn_mode_photo")
                )

                Text(
                    text = "VIDEO",
                    color = if (isVideoMode) Color(0xFFFF5252) else Color.White.copy(alpha = 0.6f),
                    fontWeight = if (isVideoMode) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { if (!isVideoMode) onToggleVideoMode() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("btn_mode_video")
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Shutter Button: Photo / Video
            val isClosed = activeRoom?.isClosed == true
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(
                        if (isClosed) Color.Gray
                        else if (isVideoMode) Color(0xFFFF5252)
                        else BentoBlue
                    )
                    .clickable(enabled = !isClosed) {
                        coroutineScope.launch {
                            if (isVideoMode) {
                                isRecording = true
                                delay(300)
                                isRecording = false
                            } else {
                                isFlashing = true
                                delay(120)
                                isFlashing = false
                            }
                            onCapture(selectedSamplePhotoRes)
                        }
                    }
                    .testTag("btn_shutter_capture")
            ) {
                Box(
                    modifier = Modifier
                        .size(if (isVideoMode) 32.dp else 64.dp)
                        .clip(if (isVideoMode) RoundedCornerShape(8.dp) else CircleShape)
                        .background(Color.White)
                        .border(3.dp, Color.Black, if (isVideoMode) RoundedCornerShape(8.dp) else CircleShape)
                )
            }
        }
    }
}
