package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ConnectsRepository
import com.example.model.CloudBackupState
import com.example.model.HighlightMovie
import com.example.model.NotificationItem
import com.example.model.PrivacyLevel
import com.example.model.RoomGroup
import com.example.model.SharedPhoto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class MainTab(val title: String, val testTag: String) {
    object Feed : MainTab("Álbum", "tab_feed")
    object Rooms : MainTab("Salas", "tab_rooms")
    object Camera : MainTab("Cámara", "tab_camera")
    object MovieStudio : MainTab("Película IA", "tab_movie_studio")
    object Profile : MainTab("Perfil", "tab_profile")
}

class ConnectsViewModel(
    val repository: ConnectsRepository = ConnectsRepository()
) : ViewModel() {

    private val _currentTab = MutableStateFlow<MainTab>(MainTab.Feed)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    val rooms: StateFlow<List<RoomGroup>> = repository.rooms
    val selectedRoomId: StateFlow<String?> = repository.selectedRoomId
    val photos: StateFlow<List<SharedPhoto>> = repository.photos
    val notifications: StateFlow<List<NotificationItem>> = repository.notifications
    val activeBanner: StateFlow<NotificationItem?> = repository.activeNotificationBanner
    val movies: StateFlow<List<HighlightMovie>> = repository.movies
    val isCloudSyncing: StateFlow<Boolean> = repository.isCloudSyncing
    val cloudBackupCount: StateFlow<Int> = repository.cloudBackupCount
    val isGeneratingAiMovie: StateFlow<Boolean> = repository.isGeneratingAiMovie

    // Filtered photos based on selected room filter in Feed
    val filteredPhotos: StateFlow<List<SharedPhoto>> = combine(photos, selectedRoomId) { photoList, activeRoomId ->
        if (activeRoomId == null) {
            photoList
        } else {
            photoList.filter { it.roomId == activeRoomId }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Photo details modal
    private val _selectedPhotoDetail = MutableStateFlow<SharedPhoto?>(null)
    val selectedPhotoDetail: StateFlow<SharedPhoto?> = _selectedPhotoDetail.asStateFlow()

    // Camera Capture state
    private val _cameraSelectedRoomId = MutableStateFlow<String>("")
    val cameraSelectedRoomId: StateFlow<String> = _cameraSelectedRoomId.asStateFlow()

    private val _cameraFilter = MutableStateFlow("Normal")
    val cameraFilter: StateFlow<String> = _cameraFilter.asStateFlow()

    private val _cameraPrivacy = MutableStateFlow(PrivacyLevel.FRIENDS_ONLY)
    val cameraPrivacy: StateFlow<PrivacyLevel> = _cameraPrivacy.asStateFlow()

    private val _cameraCaption = MutableStateFlow("")
    val cameraCaption: StateFlow<String> = _cameraCaption.asStateFlow()

    private val _isFlashEnabled = MutableStateFlow(false)
    val isFlashEnabled: StateFlow<Boolean> = _isFlashEnabled.asStateFlow()

    private val _cameraIsVideoMode = MutableStateFlow(false)
    val cameraIsVideoMode: StateFlow<Boolean> = _cameraIsVideoMode.asStateFlow()

    // Dialogs & Modals
    private val _showCreateRoomDialog = MutableStateFlow(false)
    val showCreateRoomDialog: StateFlow<Boolean> = _showCreateRoomDialog.asStateFlow()

    private val _showJoinRoomDialog = MutableStateFlow(false)
    val showJoinRoomDialog: StateFlow<Boolean> = _showJoinRoomDialog.asStateFlow()

    private val _inviteRoomId = MutableStateFlow<String?>(null)
    val inviteRoomId: StateFlow<String?> = _inviteRoomId.asStateFlow()

    private val _closeRoomConfirmId = MutableStateFlow<String?>(null)
    val closeRoomConfirmId: StateFlow<String?> = _closeRoomConfirmId.asStateFlow()

    private val _showDesktopSyncDialog = MutableStateFlow(false)
    val showDesktopSyncDialog: StateFlow<Boolean> = _showDesktopSyncDialog.asStateFlow()

    private val _showNotificationsSheet = MutableStateFlow(false)
    val showNotificationsSheet: StateFlow<Boolean> = _showNotificationsSheet.asStateFlow()

    init {
        viewModelScope.launch {
            rooms.collect { rList ->
                if (rList.isNotEmpty() && _cameraSelectedRoomId.value.isEmpty()) {
                    _cameraSelectedRoomId.value = rList.first().id
                }
            }
        }
    }

    fun setTab(tab: MainTab) {
        _currentTab.value = tab
    }

    fun selectRoomFilter(roomId: String?) {
        repository.selectRoom(roomId)
    }

    fun openPhotoDetail(photo: SharedPhoto) {
        _selectedPhotoDetail.value = photo
    }

    fun closePhotoDetail() {
        _selectedPhotoDetail.value = null
    }

    fun toggleReaction(photoId: String, emoji: String) {
        repository.toggleReaction(photoId, emoji)
        // Refresh detail if open
        _selectedPhotoDetail.value?.let { current ->
            if (current.id == photoId) {
                _selectedPhotoDetail.value = repository.photos.value.find { it.id == photoId }
            }
        }
    }

    fun addComment(photoId: String, text: String) {
        repository.addComment(photoId, text)
        // Refresh detail if open
        _selectedPhotoDetail.value?.let { current ->
            if (current.id == photoId) {
                _selectedPhotoDetail.value = repository.photos.value.find { it.id == photoId }
            }
        }
    }

    fun updatePhotoPrivacy(photoId: String, privacy: PrivacyLevel) {
        repository.updatePhotoPrivacy(photoId, privacy)
        _selectedPhotoDetail.value?.let { current ->
            if (current.id == photoId) {
                _selectedPhotoDetail.value = current.copy(privacy = privacy)
            }
        }
    }

    // Camera Actions
    fun setCameraRoom(roomId: String) {
        _cameraSelectedRoomId.value = roomId
    }

    fun setCameraFilter(filter: String) {
        _cameraFilter.value = filter
    }

    fun setCameraPrivacy(privacy: PrivacyLevel) {
        _cameraPrivacy.value = privacy
    }

    fun setCameraCaption(caption: String) {
        _cameraCaption.value = caption
    }

    fun toggleFlash() {
        _isFlashEnabled.value = !_isFlashEnabled.value
    }

    fun toggleCameraVideoMode() {
        _cameraIsVideoMode.value = !_cameraIsVideoMode.value
    }

    fun setCameraVideoMode(isVideo: Boolean) {
        _cameraIsVideoMode.value = isVideo
    }

    fun capturePhotoAndShare(sampleImageResId: Int? = null) {
        val targetRoom = _cameraSelectedRoomId.value.ifEmpty {
            rooms.value.firstOrNull()?.id ?: "room_rooftop"
        }
        
        val isVideo = _cameraIsVideoMode.value
        val duration = if (isVideo) (10..30).random() else 0

        repository.addPhotoToRoom(
            roomId = targetRoom,
            caption = _cameraCaption.value,
            filterName = _cameraFilter.value,
            privacy = _cameraPrivacy.value,
            imageResId = sampleImageResId,
            isVideo = isVideo,
            videoDurationSeconds = duration
        )

        // Reset camera form & navigate back to feed to see photo live in shared album
        _cameraCaption.value = ""
        _currentTab.value = MainTab.Feed
    }

    // Room Closing & Reopening
    fun openCloseRoomConfirm(roomId: String) {
        _closeRoomConfirmId.value = roomId
    }

    fun dismissCloseRoomConfirm() {
        _closeRoomConfirmId.value = null
    }

    fun closeRoom(roomId: String, reason: String = "Evento finalizado y archivado como recuerdo del grupo") {
        repository.closeRoom(roomId, reason)
        _closeRoomConfirmId.value = null
    }

    fun reopenRoom(roomId: String) {
        repository.reopenRoom(roomId)
    }

    // Room Invitations
    fun openInviteFriendsDialog(roomId: String) {
        _inviteRoomId.value = roomId
    }

    fun closeInviteFriendsDialog() {
        _inviteRoomId.value = null
    }

    fun inviteFriendsToRoom(roomId: String, friends: List<String>) {
        repository.inviteFriendsToRoom(roomId, friends)
        _inviteRoomId.value = null
    }

    // Room Dialogs
    fun openCreateRoomDialog() {
        _showCreateRoomDialog.value = true
    }

    fun closeCreateRoomDialog() {
        _showCreateRoomDialog.value = false
    }

    fun createRoom(name: String, description: String, privacy: PrivacyLevel, isInviteOnly: Boolean = true) {
        val newRoom = repository.createRoom(name, description, privacy, isInviteOnly)
        _cameraSelectedRoomId.value = newRoom.id
        _showCreateRoomDialog.value = false
    }

    fun openJoinRoomDialog() {
        _showJoinRoomDialog.value = true
    }

    fun closeJoinRoomDialog() {
        _showJoinRoomDialog.value = false
    }

    fun joinRoom(code: String): Boolean {
        val success = repository.joinRoomByCode(code)
        if (success) {
            _showJoinRoomDialog.value = false
        }
        return success
    }

    fun openDesktopSyncDialog() {
        _showDesktopSyncDialog.value = true
    }

    fun closeDesktopSyncDialog() {
        _showDesktopSyncDialog.value = false
    }

    fun openNotificationsSheet() {
        _showNotificationsSheet.value = true
    }

    fun closeNotificationsSheet() {
        _showNotificationsSheet.value = false
    }

    fun syncCloudBackups() {
        repository.syncAllCloudBackups()
    }

    fun generateAiMovie(roomId: String) {
        viewModelScope.launch {
            repository.generateAiMovieForRoom(roomId)
        }
    }

    fun dismissBanner() {
        repository.dismissBanner()
    }
}
