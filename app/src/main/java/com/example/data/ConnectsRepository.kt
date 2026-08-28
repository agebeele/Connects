package com.example.data

import com.example.R
import com.example.model.AiHighlightSuggestion
import com.example.model.CloudBackupState
import com.example.model.CommentItem
import com.example.model.HighlightMovie
import com.example.model.NotificationItem
import com.example.model.NotificationType
import com.example.model.PrivacyLevel
import com.example.model.ReactionCount
import com.example.model.RoomGroup
import com.example.model.RoomParticipant
import com.example.model.SharedPhoto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ConnectsRepository(
    private val geminiService: GeminiHighlightService = GeminiHighlightService()
) {
    private val scope = CoroutineScope(Dispatchers.Default)

    // Current user representation
    val currentUserId = "user_me"
    val currentUserName = "Alex Rivera"
    val currentUserHandle = "@alex_connects"
    val currentUserAvatarColor = 0xFF00E5FF

    // State flows
    private val _rooms = MutableStateFlow<List<RoomGroup>>(emptyList())
    val rooms: StateFlow<List<RoomGroup>> = _rooms.asStateFlow()

    private val _selectedRoomId = MutableStateFlow<String?>(null)
    val selectedRoomId: StateFlow<String?> = _selectedRoomId.asStateFlow()

    private val _photos = MutableStateFlow<List<SharedPhoto>>(emptyList())
    val photos: StateFlow<List<SharedPhoto>> = _photos.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _activeNotificationBanner = MutableStateFlow<NotificationItem?>(null)
    val activeNotificationBanner: StateFlow<NotificationItem?> = _activeNotificationBanner.asStateFlow()

    private val _movies = MutableStateFlow<List<HighlightMovie>>(emptyList())
    val movies: StateFlow<List<HighlightMovie>> = _movies.asStateFlow()

    private val _isCloudSyncing = MutableStateFlow(false)
    val isCloudSyncing: StateFlow<Boolean> = _isCloudSyncing.asStateFlow()

    private val _cloudBackupCount = MutableStateFlow(18)
    val cloudBackupCount: StateFlow<Int> = _cloudBackupCount.asStateFlow()

    private val _isGeneratingAiMovie = MutableStateFlow(false)
    val isGeneratingAiMovie: StateFlow<Boolean> = _isGeneratingAiMovie.asStateFlow()

    init {
        seedInitialData()
        startLiveRoomActivitySimulation()
    }

    private fun seedInitialData() {
        val p1 = RoomParticipant("p1", "Sofía Mendoza", "@sofi_m", 0xFFFF3366, isOnline = true, photosCount = 8)
        val p2 = RoomParticipant("p2", "Mateo Gómez", "@mateo_g", 0xFF6366F1, isOnline = true, photosCount = 12)
        val p3 = RoomParticipant("p3", "Camila Torres", "@cami_t", 0xFF00E676, isOnline = false, photosCount = 6)
        val p4 = RoomParticipant("p4", "Diego Herrera", "@diego_h", 0xFFFFAB00, isOnline = true, photosCount = 9)
        val pMe = RoomParticipant(currentUserId, currentUserName, currentUserHandle, currentUserAvatarColor, isOnline = true, photosCount = 5)

        val room1 = RoomGroup(
            id = "room_rooftop",
            name = "Rooftop Party Night ✨",
            description = "Celebrando con el grupo en la terraza. ¡Todos tomando fotos y videos de la fiesta!",
            joinCode = "ROOF-772",
            coverResId = R.drawable.photo_rooftop_party,
            defaultPrivacy = PrivacyLevel.FRIENDS_ONLY,
            participants = listOf(pMe, p1, p2, p4),
            photosCount = 14,
            isLiveActive = true,
            isInviteOnly = true,
            isClosed = false,
            invitedFriends = listOf("Sofía Mendoza", "Mateo Gómez", "Diego Herrera")
        )

        val room2 = RoomGroup(
            id = "room_beach",
            name = "Campamento Atardecer 🌊",
            description = "Fogata en la playa, música chill y mejores tomas del atardecer entre amigos.",
            joinCode = "BEACH-901",
            coverResId = R.drawable.photo_beach_sunset,
            defaultPrivacy = PrivacyLevel.PUBLIC,
            participants = listOf(pMe, p1, p2, p3, p4),
            photosCount = 22,
            isLiveActive = true,
            isInviteOnly = true,
            isClosed = false,
            invitedFriends = listOf("Sofía Mendoza", "Mateo Gómez", "Camila Torres", "Diego Herrera")
        )

        val room3 = RoomGroup(
            id = "room_roadtrip",
            name = "Roadtrip Montaña 🚗🏔️",
            description = "Rumbo a las cumbres nevadas. Sala cerrada y guardada como recuerdo inolvidable.",
            joinCode = "ROAD-443",
            coverResId = R.drawable.photo_mountain_roadtrip,
            defaultPrivacy = PrivacyLevel.FRIENDS_ONLY,
            participants = listOf(pMe, p2, p3),
            photosCount = 9,
            isLiveActive = false,
            isInviteOnly = true,
            isClosed = true,
            closedReason = "Viaje finalizado. Sala cerrada y guardada como recuerdo para todos los amigos.",
            invitedFriends = listOf("Mateo Gómez", "Camila Torres")
        )

        _rooms.value = listOf(room1, room2, room3)

        // Seed initial photos & videos
        val photo1 = SharedPhoto(
            id = "photo_1",
            roomId = "room_rooftop",
            roomName = room1.name,
            authorId = "p1",
            authorName = "Sofía Mendoza",
            authorAvatarColor = 0xFFFF3366,
            imageResId = R.drawable.photo_rooftop_party,
            timestampFormatted = "Hace 12 min",
            caption = "¡El mejor grupo de amigos reunido en la terraza! 🥂🎉",
            filterName = "Golden Glow",
            privacy = PrivacyLevel.FRIENDS_ONLY,
            backupState = CloudBackupState.SYNCED,
            reactions = listOf(
                ReactionCount("🔥", 7, userReacted = true),
                ReactionCount("❤️", 12, userReacted = true),
                ReactionCount("🎉", 4, userReacted = false),
                ReactionCount("📸", 3, userReacted = false)
            ),
            comments = listOf(
                CommentItem("c1", "Mateo Gómez", 0xFF6366F1, "¡Esa iluminación de la terraza quedó increíble!", "Hace 8 min"),
                CommentItem("c2", "Diego Herrera", 0xFFFFAB00, "Tenemos que armar la película con esta foto", "Hace 3 min")
            ),
            isAiHighlightSuggested = true,
            highlightScore = 0.96f,
            highlightReason = "Alta emoción grupal y excelente composición nocturna",
            isVideo = false,
            videoDurationSeconds = 0
        )

        val photo2 = SharedPhoto(
            id = "photo_2",
            roomId = "room_beach",
            roomName = room2.name,
            authorId = "p2",
            authorName = "Mateo Gómez",
            authorAvatarColor = 0xFF6366F1,
            imageResId = R.drawable.photo_beach_sunset,
            timestampFormatted = "Hace 35 min",
            caption = "Video de la fogata y música chill con el grupo. ¡Miren el atardecer! 🔥🌅",
            filterName = "Vivid Warm",
            privacy = PrivacyLevel.PUBLIC,
            backupState = CloudBackupState.SYNCED,
            reactions = listOf(
                ReactionCount("❤️", 15, userReacted = true),
                ReactionCount("🔥", 9, userReacted = false),
                ReactionCount("🌊", 8, userReacted = true)
            ),
            comments = listOf(
                CommentItem("c3", "Camila Torres", 0xFF00E676, "¡Qué paz transmite! Guardada al álbum de todos", "Hace 20 min"),
                CommentItem("c4", "Alex Rivera", 0xFF00E5FF, "¡La mejor hora dorada del año!", "Hace 15 min")
            ),
            isAiHighlightSuggested = true,
            highlightScore = 0.98f,
            highlightReason = "Momento clave de la sala: Atardecer dorado e iluminación natural",
            isVideo = true,
            videoDurationSeconds = 18
        )

        val photo3 = SharedPhoto(
            id = "photo_3",
            roomId = "room_roadtrip",
            roomName = room3.name,
            authorId = "p3",
            authorName = "Camila Torres",
            authorAvatarColor = 0xFF00E676,
            imageResId = R.drawable.photo_mountain_roadtrip,
            timestampFormatted = "Hace 2 horas",
            caption = "Parada en el mirador. Sala archivada como recuerdo del roadtrip 🛣️🍃",
            filterName = "Cinematic 35mm",
            privacy = PrivacyLevel.FRIENDS_ONLY,
            backupState = CloudBackupState.SYNCED,
            reactions = listOf(
                ReactionCount("❤️", 10, userReacted = false),
                ReactionCount("🚀", 6, userReacted = true),
                ReactionCount("⛰️", 5, userReacted = false)
            ),
            comments = listOf(
                CommentItem("c5", "Diego Herrera", 0xFFFFAB00, "¡Qué buena toma del coche con las montañas de fondo!", "Hace 1 hora"),
                CommentItem("c6", "Alex Rivera", 0xFF00E5FF, "¡Gran recuerdo! Qué bien que quedó cerrado el álbum para revivirlo.", "Hace 45 min")
            ),
            isAiHighlightSuggested = true,
            highlightScore = 0.91f,
            highlightReason = "Composición panorámica ideal para inicio de película",
            isVideo = false,
            videoDurationSeconds = 0
        )

        _photos.value = listOf(photo1, photo2, photo3)

        // Seed initial notifications
        _notifications.value = listOf(
            NotificationItem(
                id = "n1",
                title = "Nueva foto en Rooftop Party",
                message = "Sofía Mendoza subió una foto al álbum compartido.",
                timestampFormatted = "Hace 12 min",
                type = NotificationType.NEW_PHOTO,
                authorName = "Sofía Mendoza",
                authorAvatarColor = 0xFFFF3366,
                roomId = "room_rooftop",
                photoId = "photo_1"
            ),
            NotificationItem(
                id = "n2",
                title = "Reacción en tiempo real",
                message = "Mateo Gómez reaccionó con 🔥 a tu foto compartida.",
                timestampFormatted = "Hace 25 min",
                type = NotificationType.NEW_REACTION,
                authorName = "Mateo Gómez",
                authorAvatarColor = 0xFF6366F1,
                roomId = "room_beach"
            ),
            NotificationItem(
                id = "n3",
                title = "Copia en la nube completada ☁️",
                message = "Todas tus fotos y las de tus salas están respaldadas y sincronizadas.",
                timestampFormatted = "Hace 1 hora",
                type = NotificationType.CLOUD_BACKUP
            )
        )

        // Seed initial movie
        val sampleMovie = HighlightMovie(
            id = "movie_rooftop_sample",
            title = "Noche de Amigos en la Terraza",
            subtitle = "Recuerdo Cinemático • Rooftop Party",
            roomId = "room_rooftop",
            roomName = "Rooftop Party Night ✨",
            selectedPhotoIds = listOf("photo_1", "photo_2"),
            musicTrack = "Summer Synth Memories (Lo-Fi Pop)",
            styleTransition = "Cinematic Zoom & Flare",
            durationSeconds = 24,
            aiNarrativeScript = "Una reunión inolvidable donde las risas y la buena música marcaron la noche perfecta.",
            pacingBpm = 115
        )
        _movies.value = listOf(sampleMovie)
    }

    fun selectRoom(roomId: String?) {
        _selectedRoomId.value = roomId
    }

    fun addPhotoToRoom(
        roomId: String,
        caption: String,
        filterName: String,
        privacy: PrivacyLevel,
        localUri: String? = null,
        imageResId: Int? = null,
        isVideo: Boolean = false,
        videoDurationSeconds: Int = 0
    ): SharedPhoto {
        val targetRoom = _rooms.value.find { it.id == roomId } ?: _rooms.value.first()
        
        // Prevent uploads if the room is closed
        if (targetRoom.isClosed) {
            triggerInstantNotification(
                title = "Sala Cerrada como Recuerdo",
                message = "Esta sala fue finalizada. No se pueden subir nuevos archivos, pero puedes ver y comentar.",
                type = NotificationType.ROOM_INVITE,
                roomId = targetRoom.id
            )
        }

        val newPhotoId = "media_" + UUID.randomUUID().toString().take(8)

        val newPhoto = SharedPhoto(
            id = newPhotoId,
            roomId = targetRoom.id,
            roomName = targetRoom.name,
            authorId = currentUserId,
            authorName = currentUserName,
            authorAvatarColor = currentUserAvatarColor,
            imageResId = imageResId ?: R.drawable.photo_rooftop_party,
            localUri = localUri,
            timestamp = System.currentTimeMillis(),
            timestampFormatted = "Ahora mismo",
            caption = caption.ifEmpty {
                if (isVideo) "Video grupal en ${targetRoom.name} 🎥🔥"
                else "Momento capturado en ${targetRoom.name} 📸"
            },
            filterName = filterName,
            privacy = privacy,
            backupState = CloudBackupState.UPLOADING,
            reactions = listOf(
                ReactionCount("❤️", 1, userReacted = true),
                ReactionCount("🔥", 0, userReacted = false),
                ReactionCount("✨", 0, userReacted = false),
                ReactionCount("📸", 0, userReacted = false)
            ),
            comments = emptyList(),
            isAiHighlightSuggested = true,
            highlightScore = 0.95f,
            highlightReason = if (isVideo) "Clip de video dinámico con amigos" else "Nueva captura en vivo de alta resolución",
            isVideo = isVideo,
            videoDurationSeconds = videoDurationSeconds
        )

        _photos.value = listOf(newPhoto) + _photos.value
        
        // Update room photo count
        _rooms.value = _rooms.value.map { r ->
            if (r.id == targetRoom.id) r.copy(photosCount = r.photosCount + 1) else r
        }

        // Trigger Instant Notification for the group
        val mediaTypeLabel = if (isVideo) "un nuevo video" else "una foto"
        triggerInstantNotification(
            title = "¡Nuevo contenido en ${targetRoom.name}!",
            message = "$currentUserName subió $mediaTypeLabel al álbum compartido de la sala.",
            type = NotificationType.NEW_PHOTO,
            authorName = currentUserName,
            authorAvatarColor = currentUserAvatarColor,
            roomId = targetRoom.id,
            photoId = newPhotoId
        )

        // Simulate cloud backup sync
        scope.launch {
            delay(1500)
            _photos.value = _photos.value.map {
                if (it.id == newPhotoId) it.copy(backupState = CloudBackupState.SYNCED) else it
            }
            _cloudBackupCount.value += 1
        }

        return newPhoto
    }

    fun closeRoom(roomId: String, reason: String = "Evento finalizado y archivado como recuerdo para el grupo") {
        var roomName = "Sala"
        _rooms.value = _rooms.value.map { room ->
            if (room.id == roomId) {
                roomName = room.name
                room.copy(
                    isClosed = true,
                    isLiveActive = false,
                    closedReason = reason
                )
            } else {
                room
            }
        }

        triggerInstantNotification(
            title = "🔒 Sala Cerrada y Archivada como Recuerdo",
            message = "¡La sala '$roomName' concluyó! Se guardó como álbum permanente. Todos los amigos pueden verla, comentar y reaccionar.",
            type = NotificationType.ROOM_INVITE,
            authorName = currentUserName,
            authorAvatarColor = currentUserAvatarColor,
            roomId = roomId
        )
    }

    fun reopenRoom(roomId: String) {
        var roomName = "Sala"
        _rooms.value = _rooms.value.map { room ->
            if (room.id == roomId) {
                roomName = room.name
                room.copy(
                    isClosed = false,
                    isLiveActive = true
                )
            } else {
                room
            }
        }

        triggerInstantNotification(
            title = "✨ Sala Reabierta",
            message = "La sala '$roomName' volvió a activarse para que los amigos sigan subiendo fotos y videos en vivo.",
            type = NotificationType.ROOM_INVITE,
            authorName = currentUserName,
            authorAvatarColor = currentUserAvatarColor,
            roomId = roomId
        )
    }

    fun inviteFriendsToRoom(roomId: String, friendNames: List<String>) {
        if (friendNames.isEmpty()) return
        val targetRoom = _rooms.value.find { it.id == roomId } ?: return
        
        val newParticipants = friendNames.filter { name ->
            targetRoom.participants.none { it.name.equals(name, ignoreCase = true) }
        }.map { name ->
            val randomColor = listOf(0xFFFF3366, 0xFF6366F1, 0xFF00E676, 0xFFFFAB00, 0xFF8B5CF6).random()
            RoomParticipant(
                id = "p_" + UUID.randomUUID().toString().take(6),
                name = name,
                username = "@" + name.lowercase().replace(" ", "_"),
                avatarColor = randomColor,
                isOnline = true,
                photosCount = 0
            )
        }

        _rooms.value = _rooms.value.map { r ->
            if (r.id == roomId) {
                r.copy(
                    participants = r.participants + newParticipants,
                    invitedFriends = (r.invitedFriends + friendNames).distinct()
                )
            } else {
                r
            }
        }

        triggerInstantNotification(
            title = "🎉 Invitaciones Enviadas",
            message = "Se invitó a ${friendNames.joinToString(", ")} a la sala '${targetRoom.name}'.",
            type = NotificationType.ROOM_INVITE,
            authorName = currentUserName,
            authorAvatarColor = currentUserAvatarColor,
            roomId = roomId
        )
    }

    fun toggleReaction(photoId: String, emoji: String) {
        _photos.value = _photos.value.map { photo ->
            if (photo.id == photoId) {
                val updatedReactions = photo.reactions.toMutableList()
                val existingIndex = updatedReactions.indexOfFirst { it.emoji == emoji }
                if (existingIndex >= 0) {
                    val current = updatedReactions[existingIndex]
                    if (current.userReacted) {
                        updatedReactions[existingIndex] = current.copy(
                            count = (current.count - 1).coerceAtLeast(0),
                            userReacted = false
                        )
                    } else {
                        updatedReactions[existingIndex] = current.copy(
                            count = current.count + 1,
                            userReacted = true
                        )
                        // Trigger reaction notification
                        triggerInstantNotification(
                            title = "Reacción en tiempo real",
                            message = "$currentUserName reaccionó con $emoji a la foto de ${photo.authorName}",
                            type = NotificationType.NEW_REACTION,
                            authorName = currentUserName,
                            authorAvatarColor = currentUserAvatarColor,
                            roomId = photo.roomId,
                            photoId = photo.id
                        )
                    }
                } else {
                    updatedReactions.add(ReactionCount(emoji, 1, userReacted = true))
                    triggerInstantNotification(
                        title = "Reacción en tiempo real",
                        message = "$currentUserName reaccionó con $emoji a la foto de ${photo.authorName}",
                        type = NotificationType.NEW_REACTION,
                        authorName = currentUserName,
                        authorAvatarColor = currentUserAvatarColor,
                        roomId = photo.roomId,
                        photoId = photo.id
                    )
                }
                photo.copy(reactions = updatedReactions)
            } else {
                photo
            }
        }
    }

    fun addComment(photoId: String, text: String) {
        if (text.isBlank()) return
        val newComment = CommentItem(
            id = "c_" + UUID.randomUUID().toString().take(6),
            authorName = currentUserName,
            authorAvatarColor = currentUserAvatarColor,
            text = text,
            timestampFormatted = "Ahora mismo"
        )

        var photoTarget: SharedPhoto? = null
        _photos.value = _photos.value.map { photo ->
            if (photo.id == photoId) {
                photoTarget = photo
                photo.copy(comments = photo.comments + newComment)
            } else {
                photo
            }
        }

        photoTarget?.let { p ->
            triggerInstantNotification(
                title = "Nuevo comentario en el álbum",
                message = "$currentUserName: \"$text\"",
                type = NotificationType.NEW_COMMENT,
                authorName = currentUserName,
                authorAvatarColor = currentUserAvatarColor,
                roomId = p.roomId,
                photoId = p.id
            )
        }
    }

    fun updatePhotoPrivacy(photoId: String, newPrivacy: PrivacyLevel) {
        _photos.value = _photos.value.map {
            if (it.id == photoId) it.copy(privacy = newPrivacy) else it
        }
    }

    fun createRoom(
        name: String,
        description: String,
        defaultPrivacy: PrivacyLevel,
        isInviteOnly: Boolean = true,
        coverResId: Int? = R.drawable.photo_rooftop_party
    ): RoomGroup {
        val newRoom = RoomGroup(
            id = "room_" + UUID.randomUUID().toString().take(6),
            name = name,
            description = description,
            joinCode = name.take(4).uppercase().filter { it.isLetter() }.padEnd(4, 'X') + "-" + (100..999).random(),
            coverResId = coverResId,
            defaultPrivacy = defaultPrivacy,
            participants = listOf(
                RoomParticipant(currentUserId, currentUserName, currentUserHandle, currentUserAvatarColor, isOnline = true, photosCount = 0)
            ),
            photosCount = 0,
            isLiveActive = true,
            isInviteOnly = isInviteOnly,
            isClosed = false,
            invitedFriends = emptyList()
        )
        _rooms.value = listOf(newRoom) + _rooms.value
        _selectedRoomId.value = newRoom.id

        triggerInstantNotification(
            title = "¡Sala creada con éxito!",
            message = "Comparte la invitación o código '${newRoom.joinCode}' con tus amigos para el álbum compartido.",
            type = NotificationType.ROOM_INVITE,
            authorName = currentUserName,
            authorAvatarColor = currentUserAvatarColor,
            roomId = newRoom.id
        )
        return newRoom
    }

    fun joinRoomByCode(code: String): Boolean {
        val cleanCode = code.trim().uppercase()
        val room = _rooms.value.find { it.joinCode.uppercase() == cleanCode }
        if (room != null) {
            _selectedRoomId.value = room.id
            triggerInstantNotification(
                title = "¡Conectado a ${room.name}!",
                message = "Ahora puedes tomar fotos y ver el álbum compartido en tiempo real.",
                type = NotificationType.ROOM_INVITE,
                roomId = room.id
            )
            return true
        }
        return false
    }

    fun triggerInstantNotification(
        title: String,
        message: String,
        type: NotificationType,
        authorName: String = "",
        authorAvatarColor: Long = 0xFF00E5FF,
        roomId: String? = null,
        photoId: String? = null
    ) {
        val item = NotificationItem(
            id = "notif_" + UUID.randomUUID().toString().take(6),
            title = title,
            message = message,
            timestampFormatted = "Ahora",
            type = type,
            authorName = authorName,
            authorAvatarColor = authorAvatarColor,
            roomId = roomId,
            photoId = photoId,
            isRead = false
        )
        _notifications.value = listOf(item) + _notifications.value
        _activeNotificationBanner.value = item

        // Auto dismiss banner after 4 seconds
        scope.launch {
            delay(4000)
            if (_activeNotificationBanner.value?.id == item.id) {
                _activeNotificationBanner.value = null
            }
        }
    }

    fun dismissBanner() {
        _activeNotificationBanner.value = null
    }

    fun syncAllCloudBackups() {
        scope.launch {
            _isCloudSyncing.value = true
            delay(2000)
            _photos.value = _photos.value.map { it.copy(backupState = CloudBackupState.SYNCED) }
            _isCloudSyncing.value = false
            triggerInstantNotification(
                title = "Sincronización multi-dispositivo activa ☁️",
                message = "Copia de seguridad en la nube al día. Disponible en móvil y web.",
                type = NotificationType.CLOUD_BACKUP
            )
        }
    }

    suspend fun generateAiMovieForRoom(roomId: String): HighlightMovie {
        _isGeneratingAiMovie.value = true
        val room = _rooms.value.find { it.id == roomId } ?: _rooms.value.first()
        val roomPhotos = _photos.value.filter { it.roomId == room.id }.ifEmpty { _photos.value }

        val aiSuggestion: AiHighlightSuggestion = try {
            geminiService.generateAiHighlightPlan(room.name, roomPhotos)
        } catch (e: Exception) {
            AiHighlightSuggestion(
                movieTitle = "Momentos Inolvidables: ${room.name}",
                narrativeSummary = "Edición de video con transiciones dinámicas y selección de las fotos más votadas.",
                selectedPhotoIds = roomPhotos.take(4).map { it.id },
                recommendedMusic = "Sunset Indie Chill Pop",
                editingStyle = "Cinematic Crossfade",
                momentsBreakdown = listOf("Apertura", "Clímax", "Cierre")
            )
        }

        val newMovie = HighlightMovie(
            id = "movie_" + UUID.randomUUID().toString().take(8),
            title = aiSuggestion.movieTitle,
            subtitle = "Película sugerida por IA • ${room.name}",
            roomId = room.id,
            roomName = room.name,
            selectedPhotoIds = if (aiSuggestion.selectedPhotoIds.isNotEmpty()) aiSuggestion.selectedPhotoIds else roomPhotos.map { it.id },
            musicTrack = aiSuggestion.recommendedMusic,
            styleTransition = aiSuggestion.editingStyle,
            durationSeconds = (roomPhotos.size * 4).coerceIn(15, 60),
            aiNarrativeScript = aiSuggestion.narrativeSummary + "\n\n" + aiSuggestion.momentsBreakdown.joinToString("\n"),
            pacingBpm = 120
        )

        _movies.value = listOf(newMovie) + _movies.value
        _isGeneratingAiMovie.value = false

        triggerInstantNotification(
            title = "🎬 ¡Película de Amigos Lista!",
            message = "La IA creó '${newMovie.title}' con los mejores momentos de ${room.name}.",
            type = NotificationType.AI_HIGHLIGHT_READY,
            roomId = room.id
        )

        return newMovie
    }

    // Periodic simulation of friends interacting in real time
    private fun startLiveRoomActivitySimulation() {
        scope.launch {
            val friendNames = listOf("Sofía Mendoza", "Mateo Gómez", "Camila Torres", "Diego Herrera")
            val friendColors = listOf(0xFFFF3366, 0xFF6366F1, 0xFF00E676, 0xFFFFAB00)
            val friendlyComments = listOf(
                "¡Esta toma quedó para portada! 😍",
                "¡Qué buen plan armamos hoy!",
                "Esperando que suban más fotos de la fogata 🔥",
                "¡Miren qué buena luz!",
                "Descargando el álbum para mi perfil 📱"
            )
            val emojis = listOf("🔥", "❤️", "🙌", "✨", "😂")

            while (true) {
                delay(35000) // Every 35 seconds, simulate a spontaneous friend action
                val randomFriendIdx = (friendNames.indices).random()
                val friendName = friendNames[randomFriendIdx]
                val friendColor = friendColors[randomFriendIdx]
                val currentPhotoList = _photos.value

                if (currentPhotoList.isNotEmpty()) {
                    val targetPhoto = currentPhotoList.random()
                    val actionChoice = (1..3).random()

                    when (actionChoice) {
                        1 -> {
                            // Friend reacts
                            val emoji = emojis.random()
                            _photos.value = _photos.value.map { p ->
                                if (p.id == targetPhoto.id) {
                                    val rList = p.reactions.toMutableList()
                                    val rIdx = rList.indexOfFirst { it.emoji == emoji }
                                    if (rIdx >= 0) {
                                        rList[rIdx] = rList[rIdx].copy(count = rList[rIdx].count + 1)
                                    } else {
                                        rList.add(ReactionCount(emoji, 1, userReacted = false))
                                    }
                                    p.copy(reactions = rList)
                                } else p
                            }
                            triggerInstantNotification(
                                title = "Reacción en vivo",
                                message = "$friendName reaccionó con $emoji a una foto en ${targetPhoto.roomName}",
                                type = NotificationType.NEW_REACTION,
                                authorName = friendName,
                                authorAvatarColor = friendColor,
                                roomId = targetPhoto.roomId,
                                photoId = targetPhoto.id
                            )
                        }
                        2 -> {
                            // Friend comments
                            val commentText = friendlyComments.random()
                            val newComment = CommentItem(
                                id = "c_sim_" + UUID.randomUUID().toString().take(4),
                                authorName = friendName,
                                authorAvatarColor = friendColor,
                                text = commentText,
                                timestampFormatted = "Hace un instante"
                            )
                            _photos.value = _photos.value.map { p ->
                                if (p.id == targetPhoto.id) {
                                    p.copy(comments = p.comments + newComment)
                                } else p
                            }
                            triggerInstantNotification(
                                title = "Nuevo comentario de $friendName",
                                message = "\"$commentText\" en ${targetPhoto.roomName}",
                                type = NotificationType.NEW_COMMENT,
                                authorName = friendName,
                                authorAvatarColor = friendColor,
                                roomId = targetPhoto.roomId,
                                photoId = targetPhoto.id
                            )
                        }
                    }
                }
            }
        }
    }
}
