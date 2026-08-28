package com.example.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.HighlightMovie
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
import kotlinx.coroutines.delay

@Composable
fun MovieStudioScreen(
    rooms: List<RoomGroup>,
    photos: List<SharedPhoto>,
    movies: List<HighlightMovie>,
    isGeneratingAi: Boolean,
    onGenerateAiMovie: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedRoomId by remember { mutableStateOf(rooms.firstOrNull()?.id ?: "") }
    var isPlaying by remember { mutableStateOf(true) }
    var currentPhotoIndex by remember { mutableIntStateOf(0) }
    var selectedMusic by remember { mutableStateOf("Sunset Indie Beats (Chill)") }
    var selectedStyle by remember { mutableStateOf("Cinematic Zoom & Flare") }

    val activeRoom = rooms.find { it.id == selectedRoomId } ?: rooms.firstOrNull()
    val availablePhotos = photos.filter { it.roomId == activeRoom?.id }.ifEmpty { photos }

    val currentMovie = movies.find { it.roomId == activeRoom?.id } ?: movies.firstOrNull()

    // Ken Burns Animation
    val infiniteTransition = rememberInfiniteTransition(label = "ken_burns")
    val zoomScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "zoomScale"
    )

    // Playback loop timer
    LaunchedEffect(isPlaying, availablePhotos.size) {
        if (availablePhotos.isNotEmpty()) {
            while (isPlaying) {
                delay(3500)
                currentPhotoIndex = (currentPhotoIndex + 1) % availablePhotos.size
            }
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        // Header
        item {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Movie,
                        contentDescription = null,
                        tint = BentoBlue,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Estudio Película de Amigos",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoTextPrimary
                        )
                    )
                }
                Text(
                    text = "Montajes automáticos con IA y estilo cinematográfico",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = BentoTextSecondary
                    )
                )
            }
        }

        // Room Selector Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(rooms) { room ->
                    val isSelected = room.id == selectedRoomId
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) BentoBlue else Color.White,
                        border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight) else null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                selectedRoomId = room.id
                                currentPhotoIndex = 0
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = room.name,
                                color = if (isSelected) Color.White else BentoTextPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Movie Player Canvas (Cinematic Viewport)
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BentoBorderLight, RoundedCornerShape(24.dp))
                    .testTag("movie_player_canvas")
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 10f)
                            .background(Color.Black)
                    ) {
                        if (availablePhotos.isNotEmpty()) {
                            val activePhoto = availablePhotos[currentPhotoIndex.coerceIn(0, availablePhotos.size - 1)]
                            val res = activePhoto.imageResId ?: R.drawable.photo_rooftop_party

                            Crossfade(
                                targetState = res,
                                animationSpec = tween(800),
                                label = "movie_crossfade"
                            ) { photoRes ->
                                Image(
                                    painter = painterResource(id = photoRes),
                                    contentDescription = "Movie Scene",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .scale(if (isPlaying) zoomScale else 1.05f)
                                )
                            }

                            // Cinematic Letterbox Top & Bottom Bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .align(Alignment.TopCenter)
                                    .background(Color.Black.copy(alpha = 0.8f))
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .align(Alignment.BottomCenter)
                                    .background(Color.Black.copy(alpha = 0.8f))
                            )

                            // Title & Soundtrack Badge
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.Black.copy(alpha = 0.7f),
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(start = 12.dp, top = 26.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MusicNote,
                                        contentDescription = null,
                                        tint = BentoCardBlue,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = selectedMusic,
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            // Subtitle Caption on Scene
                            if (activePhoto.caption.isNotBlank()) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color.Black.copy(alpha = 0.75f),
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(bottom = 28.dp, start = 16.dp, end = 16.dp)
                                ) {
                                    Text(
                                        text = "\"${activePhoto.caption}\"",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Player Controls Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BentoDarkNavy)
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { isPlaying = !isPlaying },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(BentoBlue)
                                    .testTag("btn_movie_play_pause")
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "Pausar" else "Reproducir",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            IconButton(
                                onClick = { currentPhotoIndex = 0 },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Replay,
                                    contentDescription = "Reiniciar",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Progress indicator
                        val progress = if (availablePhotos.isNotEmpty()) {
                            (currentPhotoIndex + 1).toFloat() / availablePhotos.size.toFloat()
                        } else 0f

                        LinearProgressIndicator(
                            progress = { progress },
                            color = BentoCardBlue,
                            trackColor = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp)
                                .height(4.dp)
                                .clip(CircleShape)
                        )

                        Text(
                            text = "${currentPhotoIndex + 1} / ${availablePhotos.size}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // AI Gemini Auto-Generation Bento Action Card
        item {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = BentoCardPurpleSubtle,
                border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = BentoBlue,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Auto-Edición Inteligente con IA",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoTextPrimary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Gemini analiza las expresiones, iluminación y mejores comentarios del álbum grupal para sugerir la película perfecta.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = BentoTextSecondary,
                            fontSize = 12.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onGenerateAiMovie(activeRoom?.id ?: "") },
                        enabled = !isGeneratingAi,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BentoBlue
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("btn_generate_ai_movie")
                    ) {
                        if (isGeneratingAi) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Curando Momentos con Gemini...",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Sugerir Película con IA para ${activeRoom?.name ?: "Sala"}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // AI Narrative Script Box if generated
        if (currentMovie != null) {
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorderLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = currentMovie.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoTextPrimary
                                )
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = BentoCardBlue
                            ) {
                                Text(
                                    text = "${currentMovie.durationSeconds}s",
                                    color = BentoBlue,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = currentMovie.aiNarrativeScript,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BentoTextSecondary,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }
        }

        // Soundtrack & Pacing Options
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Banda Sonora del Recuerdo",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = BentoTextPrimary
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                val tracks = listOf(
                    "Sunset Indie Beats (Chill)",
                    "Summer Pop Memories",
                    "Synthwave Nostalgia",
                    "Cinematic Acoustic Loop"
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(tracks) { track ->
                        val isSelected = selectedMusic == track
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) BentoBlue else BentoCardLavenderGray,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { selectedMusic = track }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MusicNote,
                                    contentDescription = null,
                                    tint = if (isSelected) Color.White else BentoTextSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = track,
                                    color = if (isSelected) Color.White else BentoTextPrimary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Transitions Style
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Estilo de Transición",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = BentoTextPrimary
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                val styles = listOf(
                    "Cinematic Zoom & Flare",
                    "Retro 35mm Fade",
                    "Dynamic Crossfade",
                    "Glitch Pop Beat"
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(styles) { st ->
                        val isSelected = selectedStyle == st
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) BentoBlue else BentoCardLavenderGray,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { selectedStyle = st }
                        ) {
                            Text(
                                text = st,
                                color = if (isSelected) Color.White else BentoTextPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

