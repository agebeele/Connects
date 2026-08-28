package com.example.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.ConnectsBottomNav
import com.example.ui.components.ConnectsTopBar
import com.example.ui.components.DesktopSyncDialog
import com.example.ui.components.LiveNotificationBanner
import com.example.ui.components.NotificationsBottomSheet
import com.example.ui.components.PhotoDetailDialog
import com.example.ui.screens.CameraCaptureScreen
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.MovieStudioScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RoomsScreen
import com.example.ui.viewmodel.ConnectsViewModel
import com.example.ui.viewmodel.MainTab

@Composable
fun ConnectsApp(
    viewModel: ConnectsViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val rooms by viewModel.rooms.collectAsState()
    val selectedRoomId by viewModel.selectedRoomId.collectAsState()
    val filteredPhotos by viewModel.filteredPhotos.collectAsState()
    val allPhotos by viewModel.photos.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val activeBanner by viewModel.activeBanner.collectAsState()
    val movies by viewModel.movies.collectAsState()
    val isCloudSyncing by viewModel.isCloudSyncing.collectAsState()
    val cloudBackupCount by viewModel.cloudBackupCount.collectAsState()
    val isGeneratingAiMovie by viewModel.isGeneratingAiMovie.collectAsState()

    val selectedPhotoDetail by viewModel.selectedPhotoDetail.collectAsState()
    val cameraSelectedRoomId by viewModel.cameraSelectedRoomId.collectAsState()
    val cameraFilter by viewModel.cameraFilter.collectAsState()
    val cameraPrivacy by viewModel.cameraPrivacy.collectAsState()
    val cameraCaption by viewModel.cameraCaption.collectAsState()
    val isFlashEnabled by viewModel.isFlashEnabled.collectAsState()
    val cameraIsVideoMode by viewModel.cameraIsVideoMode.collectAsState()

    val showCreateRoomDialog by viewModel.showCreateRoomDialog.collectAsState()
    val showJoinRoomDialog by viewModel.showJoinRoomDialog.collectAsState()
    val inviteRoomId by viewModel.inviteRoomId.collectAsState()
    val closeRoomConfirmId by viewModel.closeRoomConfirmId.collectAsState()
    val showDesktopSyncDialog by viewModel.showDesktopSyncDialog.collectAsState()
    val showNotificationsSheet by viewModel.showNotificationsSheet.collectAsState()

    val unreadNotificationsCount = notifications.count { !it.isRead }

    Scaffold(
        topBar = {
            if (currentTab != MainTab.Camera) {
                ConnectsTopBar(
                    unreadNotificationsCount = unreadNotificationsCount,
                    isCloudSyncing = isCloudSyncing,
                    onOpenNotifications = { viewModel.openNotificationsSheet() },
                    onOpenDesktopSync = { viewModel.openDesktopSyncDialog() }
                )
            }
        },
        bottomBar = {
            ConnectsBottomNav(
                currentTab = currentTab,
                onTabSelected = { tab -> viewModel.setTab(tab) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Screen contents
            when (currentTab) {
                MainTab.Feed -> {
                    FeedScreen(
                        rooms = rooms,
                        photos = filteredPhotos,
                        selectedRoomId = selectedRoomId,
                        cloudBackupCount = cloudBackupCount,
                        onSelectRoom = { rId -> viewModel.selectRoomFilter(rId) },
                        onPhotoClick = { photo -> viewModel.openPhotoDetail(photo) },
                        onToggleReaction = { pId, emoji -> viewModel.toggleReaction(pId, emoji) },
                        onOpenComments = { photo -> viewModel.openPhotoDetail(photo) },
                        onChangePrivacy = { pId, priv -> viewModel.updatePhotoPrivacy(pId, priv) },
                        onCreateRoom = { viewModel.openCreateRoomDialog() },
                        onLaunchCamera = { viewModel.setTab(MainTab.Camera) },
                        onOpenMovieStudio = { viewModel.setTab(MainTab.MovieStudio) },
                        onOpenDesktopSync = { viewModel.openDesktopSyncDialog() }
                    )
                }
                MainTab.Rooms -> {
                    RoomsScreen(
                        rooms = rooms,
                        onSelectRoom = { rId ->
                            viewModel.selectRoomFilter(rId)
                            viewModel.setTab(MainTab.Feed)
                        },
                        onLaunchCameraForRoom = { rId ->
                            viewModel.setCameraRoom(rId)
                            viewModel.setTab(MainTab.Camera)
                        },
                        onGenerateMovieForRoom = { rId ->
                            viewModel.generateAiMovie(rId)
                            viewModel.setTab(MainTab.MovieStudio)
                        },
                        showCreateDialog = showCreateRoomDialog,
                        onOpenCreateDialog = { viewModel.openCreateRoomDialog() },
                        onCloseCreateDialog = { viewModel.closeCreateRoomDialog() },
                        onCreateRoom = { name, desc, priv, inviteOnly -> viewModel.createRoom(name, desc, priv, inviteOnly) },
                        showJoinDialog = showJoinRoomDialog,
                        onOpenJoinDialog = { viewModel.openJoinRoomDialog() },
                        onCloseJoinDialog = { viewModel.closeJoinRoomDialog() },
                        onJoinRoom = { code -> viewModel.joinRoom(code) },
                        inviteRoomId = inviteRoomId,
                        onOpenInviteDialog = { rId -> viewModel.openInviteFriendsDialog(rId) },
                        onCloseInviteDialog = { viewModel.closeInviteFriendsDialog() },
                        onInviteFriends = { rId, friends -> viewModel.inviteFriendsToRoom(rId, friends) },
                        closeConfirmRoomId = closeRoomConfirmId,
                        onOpenCloseConfirm = { rId -> viewModel.openCloseRoomConfirm(rId) },
                        onDismissCloseConfirm = { viewModel.dismissCloseRoomConfirm() },
                        onConfirmCloseRoom = { rId -> viewModel.closeRoom(rId) },
                        onReopenRoom = { rId -> viewModel.reopenRoom(rId) }
                    )
                }
                MainTab.Camera -> {
                    CameraCaptureScreen(
                        rooms = rooms,
                        selectedRoomId = cameraSelectedRoomId,
                        filter = cameraFilter,
                        privacy = cameraPrivacy,
                        caption = cameraCaption,
                        isFlashEnabled = isFlashEnabled,
                        isVideoMode = cameraIsVideoMode,
                        onSelectRoom = { rId -> viewModel.setCameraRoom(rId) },
                        onSelectFilter = { f -> viewModel.setCameraFilter(f) },
                        onSelectPrivacy = { p -> viewModel.setCameraPrivacy(p) },
                        onCaptionChange = { c -> viewModel.setCameraCaption(c) },
                        onToggleFlash = { viewModel.toggleFlash() },
                        onToggleVideoMode = { viewModel.toggleCameraVideoMode() },
                        onCapture = { sampleRes -> viewModel.capturePhotoAndShare(sampleRes) }
                    )
                }
                MainTab.MovieStudio -> {
                    MovieStudioScreen(
                        rooms = rooms,
                        photos = allPhotos,
                        movies = movies,
                        isGeneratingAi = isGeneratingAiMovie,
                        onGenerateAiMovie = { rId -> viewModel.generateAiMovie(rId) }
                    )
                }
                MainTab.Profile -> {
                    ProfileScreen(
                        rooms = rooms,
                        photos = allPhotos,
                        cloudBackupCount = cloudBackupCount,
                        onOpenDesktopSync = { viewModel.openDesktopSyncDialog() },
                        onPhotoClick = { photo -> viewModel.openPhotoDetail(photo) }
                    )
                }
            }

            // Real-time Floating Dropdown Banner
            LiveNotificationBanner(
                notification = activeBanner,
                onDismiss = { viewModel.dismissBanner() },
                onClick = { notif ->
                    viewModel.dismissBanner()
                    viewModel.openNotificationsSheet()
                }
            )
        }
    }

    // Photo Detail Dialog
    selectedPhotoDetail?.let { photo ->
        PhotoDetailDialog(
            photo = photo,
            onDismiss = { viewModel.closePhotoDetail() },
            onToggleReaction = { emoji -> viewModel.toggleReaction(photo.id, emoji) },
            onAddComment = { text -> viewModel.addComment(photo.id, text) },
            onChangePrivacy = { priv -> viewModel.updatePhotoPrivacy(photo.id, priv) }
        )
    }

    // Desktop Sync & Cloud Backup Modal
    if (showDesktopSyncDialog) {
        DesktopSyncDialog(
            isSyncing = isCloudSyncing,
            backedUpPhotosCount = cloudBackupCount,
            onSyncNow = { viewModel.syncCloudBackups() },
            onDismiss = { viewModel.closeDesktopSyncDialog() }
        )
    }

    // Notifications Bottom Sheet
    if (showNotificationsSheet) {
        NotificationsBottomSheet(
            notifications = notifications,
            onDismiss = { viewModel.closeNotificationsSheet() },
            onNotificationClick = { notif ->
                viewModel.closeNotificationsSheet()
                if (notif.photoId != null) {
                    val targetPhoto = allPhotos.find { it.id == notif.photoId }
                    if (targetPhoto != null) {
                        viewModel.openPhotoDetail(targetPhoto)
                    }
                }
            }
        )
    }
}
