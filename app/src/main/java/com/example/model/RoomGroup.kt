package com.example.model

enum class PrivacyLevel(val label: String, val iconName: String) {
    PUBLIC("Público", "Public"),
    FRIENDS_ONLY("Solo Amigos", "People"),
    PRIVATE("Privado", "Lock")
}

data class RoomParticipant(
    val id: String,
    val name: String,
    val username: String,
    val avatarColor: Long,
    val isOnline: Boolean = true,
    val photosCount: Int = 0
)

data class RoomGroup(
    val id: String,
    val name: String,
    val description: String,
    val joinCode: String,
    val coverResId: Int?,
    val defaultPrivacy: PrivacyLevel = PrivacyLevel.FRIENDS_ONLY,
    val participants: List<RoomParticipant> = emptyList(),
    val photosCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val isLiveActive: Boolean = true,
    val isInviteOnly: Boolean = true,
    val isClosed: Boolean = false,
    val closedReason: String = "Evento finalizado y archivado como recuerdo del grupo",
    val invitedFriends: List<String> = emptyList()
)
