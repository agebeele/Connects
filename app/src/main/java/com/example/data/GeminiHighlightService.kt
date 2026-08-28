package com.example.data

import android.util.Log
import com.example.BuildConfig
import com.example.model.AiHighlightSuggestion
import com.example.model.SharedPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiHighlightService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun generateAiHighlightPlan(
        roomName: String,
        photos: List<SharedPhoto>
    ): AiHighlightSuggestion = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        
        // Build prompt with photo context
        val photoSummaries = photos.mapIndexed { idx, p ->
            "Foto #${idx + 1} (ID: ${p.id}): '${p.caption}' por ${p.authorName}, Filtro: ${p.filterName}, Reacciones: ${p.reactions.sumOf { it.count }}"
        }.joinToString("\n")

        val prompt = """
            Eres un director de cine experto y editor de video para la red social 'Connects'.
            Analiza las siguientes fotos del grupo/sala '$roomName':
            $photoSummaries
            
            Genera una edición automática en 'estilo película' de momentos destacados para este grupo de amigos.
            Responde ÚNICAMENTE con un JSON válido en español con la siguiente estructura:
            {
              "movieTitle": "Título cinematográfico emocionante del recuerdo",
              "narrativeSummary": "Descripción breve del estilo visual y la historia de la película de amigos",
              "recommendedMusic": "Canción o estilo musical sugerido (ej: Synthwave Nostalgia, Summer Indie Beats, Cinematic Acoustic)",
              "editingStyle": "Cinematic Zoom / Dynamic Crossfade / Retro Film Grain",
              "momentsBreakdown": [
                "Momento 1: Apertura con energía y risas",
                "Momento 2: Clímax del atardecer y celebración",
                "Momento 3: Cierre nostálgico con todos los amigos"
              ]
            }
        """.trimIndent()

        if (apiKey.isNotEmpty() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val jsonBody = JSONObject().apply {
                    val contents = org.json.JSONArray().apply {
                        val contentObj = JSONObject().apply {
                            val parts = org.json.JSONArray().apply {
                                val partObj = JSONObject().apply {
                                    put("text", prompt)
                                }
                                put(partObj)
                            }
                            put("parts", parts)
                        }
                        put(contentObj)
                    }
                    put("contents", contents)
                    
                    val generationConfig = JSONObject().apply {
                        put("temperature", 0.7)
                        val responseFormat = JSONObject().apply {
                            put("mimeType", "application/json")
                        }
                        put("responseFormat", responseFormat)
                    }
                    put("generationConfig", generationConfig)
                }

                val request = Request.Builder()
                    .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                    .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    val rootJson = JSONObject(responseBody)
                    val candidates = rootJson.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val firstCandidate = candidates.getJSONObject(0)
                        val content = firstCandidate.getJSONObject("content")
                        val parts = content.getJSONArray("parts")
                        val text = parts.getJSONObject(0).getString("text")
                        
                        val parsed = JSONObject(text)
                        val momentsJsonArray = parsed.optJSONArray("momentsBreakdown")
                        val momentsList = mutableListOf<String>()
                        if (momentsJsonArray != null) {
                            for (i in 0 until momentsJsonArray.length()) {
                                momentsList.add(momentsJsonArray.getString(i))
                            }
                        }

                        return@withContext AiHighlightSuggestion(
                            movieTitle = parsed.optString("movieTitle", "Nuestra Mejor Aventura: $roomName"),
                            narrativeSummary = parsed.optString("narrativeSummary", "Un montaje dinámico lleno de risas, miradas cómplices y momentos inolvidables."),
                            selectedPhotoIds = photos.map { it.id },
                            recommendedMusic = parsed.optString("recommendedMusic", "Summer Sunset Nostalgia (Chill Beats)"),
                            editingStyle = parsed.optString("editingStyle", "Cinematic Zoom & Flare"),
                            momentsBreakdown = if (momentsList.isNotEmpty()) momentsList else listOf(
                                "00:00 - Llegada y primeras risas",
                                "00:03 - Tomas espontáneas en grupo",
                                "00:08 - Brindis y foto grupal perfecta"
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("GeminiHighlightService", "Error calling Gemini API: ${e.message}", e)
            }
        }

        // Fallback intelligent curated highlight
        val fallbackTitle = when {
            roomName.contains("Party", ignoreCase = true) || roomName.contains("Cumple", ignoreCase = true) -> "Noche Legendaria: $roomName"
            roomName.contains("Beach", ignoreCase = true) || roomName.contains("Playa", ignoreCase = true) -> "Golden Hour & Olas Infinitas"
            roomName.contains("Roadtrip", ignoreCase = true) || roomName.contains("Viaje", ignoreCase = true) -> "Ruta de Amigos: Kilómetros de Recuerdos"
            else -> "Crónicas de $roomName • Connects Highlight"
        }

        return@withContext AiHighlightSuggestion(
            movieTitle = fallbackTitle,
            narrativeSummary = "Edición cinematográfica con ritmo dinámico, corrección de color cálido y transición rítmica adaptada al compás musical.",
            selectedPhotoIds = photos.take(4).map { it.id },
            recommendedMusic = "Indie Dream Pop & Sunset Chill",
            editingStyle = "Cinematic Crossfade + Slow Zoom",
            momentsBreakdown = listOf(
                "Escena 1: El inicio perfecto - Risas espontáneas",
                "Escena 2: Atardecer dorado capturado en conjunto",
                "Escena 3: Momento estelar con todas las reacciones activas",
                "Escena 4: Cierre emotivo y recuerdo compartido"
            )
        )
    }
}
