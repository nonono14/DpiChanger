# DPI Changer for Wear OS

Приложение для Wear OS, которое меняет системный DPI без root, плюс
опциональный LSPosed-модуль для точечной подмены плотности экрана
у отдельных сторонних приложений (например, чтобы APK, рассчитанный
на телефон, лучше вписывался в маленький экран часов).



![platform](https://img.shields.io/badge/platform-Wear%20OS-blue)




![language](https://img.shields.io/badge/language-Kotlin-orange)




![license](https://img.shields.io/badge/license-MIT-green)



## Возможности

- 📐 Смена системного DPI прямо с часов, без компьютера после первой настройки
- 🔄 Быстрый сброс на заводское значение плотности
- 🎯 (опционально, требует root + LSPosed) Подмена DPI только для одного
  выбранного стороннего приложения, без влияния на систему в целом
- ☁️ Сборка APK через GitHub Actions — не нужен Android Studio

## Требования

| Модуль | Требования |
|---|---|
| `app` (смена системного DPI) | Wear OS, разрешение `WRITE_SECURE_SETTINGS` через ADB (без root) |
| `xposed-module` (per-app override) | Root + установленный LSPosed framework на часах |

## Установка

\```bash
adb install app-debug.apk
adb shell pm grant com.zhenya.dpichanger android.permission.WRITE_SECURE_SETTINGS
\```

Подробная пошаговая инструкция (включая сборку через GitHub Actions
без компьютера) — в [SETUP.md](./SETUP.md).

## Как это работает

`app` использует тот же скрытый системный API
(`IWindowManager.setForcedDisplayDensityForUser`), который дёргает
команда `adb shell wm density`, только изнутри самого приложения через
reflection.

`xposed-module` хукает `Application.attach()` целевого приложения и
подменяет `Configuration.densityDpi` и `DisplayMetrics` до того, как
приложение успевает построить свой UI — так плотность меняется только
для него, а не для всей системы.

## Дисклеймер

Даже с подменой плотности стороннее приложение может не влезть в экран
часов идеально — многое зависит от того, насколько жёстко у него
зашиты размеры layout и элементы навигации. Это инструмент для
масштабирования, а не гарантированная адаптация любого APK под круглый
или квадратный экран.

## Лицензия

MIT — используй как хочешь.
