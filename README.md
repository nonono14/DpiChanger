<div align="center">

# ⌚ DPI Changer for Wear OS

**Меняй системный DPI на часах без root — и точечно ужимай сторонние приложения под маленький экран**

![platform](https://img.shields.io/badge/platform-Wear%20OS-4285F4?style=flat-square&logo=android&logoColor=white)
![language](https://img.shields.io/badge/language-Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![design](https://img.shields.io/badge/design-Material%20You-FF6F91?style=flat-square)
![license](https://img.shields.io/badge/license-MIT-2ECC71?style=flat-square)

</div>

---

## 📖 О проекте

Два независимых модуля в одном репозитории:

| Модуль | Что делает | Требования |
|---|---|---|
| **`app`** | Меняет системный DPI прямо с часов, пресетами или сбросом на заводское | Wear OS, разовый ADB-грант, **без root** |
| **`xposed-module`** | Подменяет DPI только для одного стороннего APK — чтобы телефонное приложение лучше вписалось в маленький экран часов | **Root + LSPosed** |

---

## ✨ Возможности

- 📐 Быстрая смена DPI пресетами (160–320) прямо на экране часов
- 🔄 Сброс на заводскую плотность одним нажатием
- 🎨 Material You — динамические цвета из системной темы часов
- 🎯 Точечная подмена плотности для одного приложения, без влияния на систему
- ☁️ Сборка APK через GitHub Actions — Android Studio не нужен

---

## 🚀 Быстрый старт

```bash
# ставим app
adb install app-debug.apk
adb shell pm grant com.zhenya.dpichanger android.permission.WRITE_SECURE_SETTINGS
```

Открываешь приложение на часах → жмёшь нужный DPI → применяется мгновенно.

Полная инструкция, включая сборку без компьютера через GitHub Actions — в [SETUP.md](./SETUP.md).

---

## ⚙️ Как это работает

**`app`** дёргает скрытый системный API `IWindowManager.setForcedDisplayDensityForUs
