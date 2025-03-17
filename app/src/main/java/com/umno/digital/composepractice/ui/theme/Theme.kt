package com.umno.digital.composepractice.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CyberDarkColorScheme = darkColorScheme(
    primary = CyberYellow,          // Основной цвет - фирменный жёлтый
    onPrimary = CyberDarkTitle,     // Текст на основном цвете - тёмный для заголовков
    secondary = CyberGreen,         // Второстепенный цвет - кислотно-зелёный
    onSecondary = CyberDarkText,    // Текст на втором цвете - светлый
    tertiary = CyberPink,           // Третичный цвет - неоновый розовый
    onTertiary = CyberDarkText,     // Текст на третичном цвете
    background = CyberDarkBg,       // Фон - почти чёрный
    onBackground = CyberGreen,      // Текст на фоне - кислотно-зелёный
    surface = CyberDarkSurface,     // Поверхности - тёмно-серый
    onSurface = CyberYellow,        // Текст на поверхностях - фирменный жёлтый
    surfaceVariant = CyberDarkCard, // Вариант поверхности для карточек
    onSurfaceVariant = CyberInputText, // Текст в полях ввода - зелёный
    error = CyberError,             // Цвет ошибки
    onError = CyberDarkText,        // Текст на ошибке
    scrim = CyberSelectedDark,      // Цвет выделения
    outlineVariant = CyberSelectedTextDark // Цвет текста на выделении
)

private val CyberLightColorScheme = lightColorScheme(
    primary = CyberGreen,           // Основной цвет - кислотно-зелёный
    onPrimary = CyberLightTitle,    // Текст на основном цвете - светлый для заголовков
    secondary = CyberSelectedDark,  // Второстепенный цвет - жёлтый
    onSecondary = CyberLightText,   // Текст на втором цвете
    tertiary = CyberBlue,           // Третичный цвет - голубой
    onTertiary = CyberLightText,    // Текст на третичном цвете
    background = CyberLightBg,      // Фон - светлый
    onBackground = CyberLightText,  // Текст на фоне
    surface = CyberLightSurface,    // Поверхности
    onSurface = CyberLightText,     // Текст на поверхностях
    surfaceVariant = CyberLightCard, // Вариант поверхности
    onSurfaceVariant = CyberInputTextLight, // Текст в полях ввода
    error = CyberError,             // Цвет ошибки
    onError = CyberLightText,       // Текст на ошибке
    scrim = CyberSelectedLight,     // Цвет выделения
    outlineVariant = CyberSelectedTextLight // Цвет текста на выделении
)

@Composable
fun ComposePracticeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Отключаем динамические цвета для сохранения стиля Cyberpunk
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> CyberDarkColorScheme
        else -> CyberLightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Устанавливаем цвет статус-бара в соответствии с темой
            window.statusBarColor = if (darkTheme) CyberDarkBg.toArgb() else CyberLightBg.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}