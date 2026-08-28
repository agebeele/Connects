package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BentoBlue,
    onPrimary = Color.White,
    primaryContainer = BentoCardBlue,
    onPrimaryContainer = BentoDarkNavy,
    secondary = BentoPurpleIcon,
    onSecondary = Color.White,
    secondaryContainer = BentoPurpleChip,
    onSecondaryContainer = BentoPurpleIcon,
    tertiary = AmberGlow,
    onTertiary = Color.Black,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = Color(0xFF49454F),
    outlineVariant = Color(0xFF33353A)
)

private val LightColorScheme = lightColorScheme(
    primary = BentoBlue,
    onPrimary = Color.White,
    primaryContainer = BentoCardBlue,
    onPrimaryContainer = BentoDarkNavy,
    secondary = BentoPurpleIcon,
    onSecondary = Color.White,
    secondaryContainer = BentoPurpleChip,
    onSecondaryContainer = BentoPurpleIcon,
    tertiary = AmberGlow,
    onTertiary = Color.White,
    background = BentoCanvasBg,
    onBackground = BentoTextPrimary,
    surface = Color.White,
    onSurface = BentoTextPrimary,
    surfaceVariant = BentoCardLavenderGray,
    onSurfaceVariant = BentoTextSecondary,
    outline = BentoBorder,
    outlineVariant = BentoBorderLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

