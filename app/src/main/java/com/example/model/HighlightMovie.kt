package com.example.model

data class HighlightMovie(
    val id: String,
    val title: String,
    val subtitle: String,
    val roomId: String,
    val roomName: String,
    val selectedPhotoIds: List<String>,
    val musicTrack: String,
    val styleTransition: String, // "Cinematic Zoom", "Retro Fade", "Glitch Pop", "Dynamic Crossfade"
    val durationSeconds: Int,
    val aiNarrativeScript: String,
    val pacingBpm: Int = 120,
    val createdAt: Long = System.currentTimeMillis()
)

data class AiHighlightSuggestion(
    val movieTitle: String,
    val narrativeSummary: String,
    val selectedPhotoIds: List<String>,
    val recommendedMusic: String,
    val editingStyle: String,
    val momentsBreakdown: List<String>
)
