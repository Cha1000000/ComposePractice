package com.umno.digital.composepractice.ui.theme

import androidx.compose.ui.graphics.Color

// Основные цвета Cyberpunk
val CyberYellow = Color(0xFFFFF100)      // Фирменный жёлтый Cyberpunk
val CyberGreen = Color(0xFF00FF41)       // Кислотно-зелёный
val CyberPink = Color(0xFFFF006B)        // Неоновый розовый
val CyberBlue = Color(0xFF00FFFF)        // Неоновый голубой

// Темная тема
val CyberDarkBg = Color(0xFF0D0D0D)          // Почти чёрный фон
val CyberDarkSurface = Color(0xFF1A1A1A)     // Тёмно-серый для поверхностей
val CyberDarkCard = Color(0xFF242424)        // Чуть светлее для карточек
val CyberDarkText = Color(0xFFE0E0E0)        // Светлый текст
val CyberDarkSecondary = Color(0xFF3A3A3A)   // Для второстепенных элементов
val CyberDarkTitle = Color(0xFF0D0D0D)       // Цвет текста заголовков в тёмной теме

// Светлая тема
val CyberLightBg = Color(0xFFF5F5F5)         // Светлый фон
val CyberLightSurface = Color(0xFFE8E8E8)    // Поверхности
val CyberLightCard = Color(0xFFD8D8D8)       // Карточки
val CyberLightText = Color(0xFF1A1A1A)       // Тёмный текст
val CyberLightSecondary = Color(0xFFB0B0B0)  // Второстепенные элементы
val CyberLightTitle = CyberPink              // Цвет текста заголовков в светлой теме

// Состояния элементов
val CyberSelectedDark = Color(0xFF002A3D)    // Выделение в тёмной теме (тёмно-синий)
val CyberSelectedLight = Color(0xFFE5F6FF)   // Выделение в светлой теме (светло-голубой)
val CyberSelectedTextDark = CyberBlue        // Цвет текста на выделении в тёмной теме
val CyberSelectedTextLight = Color(0xFF1A1A1A) // Цвет текста на выделении в светлой теме
val CyberInputText = CyberGreen              // Цвет текста в полях ввода (тёмная тема)
val CyberInputTextLight = CyberDarkBg        // Цвет текста в полях ввода (светлая тема)

// Акцентные цвета
val CyberError = Color(0xFFFF1744)           // Ошибки
val CyberSuccess = CyberGreen                // Успех
val CyberWarning = CyberYellow               // Предупреждения