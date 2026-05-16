# FlowTab-CMP — Deep Analysis
> Last updated: May 16, 2026 · Version `0.5.6-beta`

---

## Architecture Map

```
flow-tab/src/commonMain/
│
├── BottomNavigation.kt              ← Public API entry point
│
├── domain/model/
│   ├── NavItem.kt                   ← Item data model
│   ├── NavItemType.kt               ← Sealed type: Standard | Search | Isolated
│   ├── NavConfig.kt                 ← Global appearance config
│   ├── NavColor.kt                  ← Color scheme
│   ├── NavIndicator.kt              ← Sealed indicator: Ripple | Dot | Line
│   ├── BadgeData.kt                 ← Badge model (count / dot)
│   ├── NavState.kt                  ← Internal animation state
│   └── NavStateSaver.kt             ← rememberSaveable saver
│
├── ui/
│   ├── container/
│   │   └── BottomNavContent.kt      ← Layout orchestrator
│   ├── section/
│   │   ├── StandardNavContainer.kt  ← Main nav row + indicator
│   │   ├── SearchSection.kt         ← Expandable search bar
│   │   └── IsolatedSection.kt       ← Floating action button
│   ├── component/
│   │   ├── navitem/NavItemView.kt   ← Single nav item (icon + label + badge)
│   │   └── badge/Badge.kt           ← Badge widget
│   └── extension/
│       └── BlurExt.kt               ← Haze modifier extension
│
└── util/math/
    └── Interpolate.kt               ← Linear Dp range mapper
```

---

## ✅ What's Perfect

### 1. Zero Navigation Dependency
`BottomNavigation` takes a plain `String selectedId` and emits `NavItem` through a callback. No Jetpack Navigation, no Decompose, no Voyager — consumers wire it to anything they like.

### 2. Fully Typed, Composition-Safe Models
Every public model (`NavItem`, `NavConfig`, `NavColor`, `NavIndicator`, `BadgeData`, `NavItemType`) is annotated `@Immutable` or `@Stable`. Compose's smart-recomposition skips unchanged subtrees correctly.

### 3. `returnToId` — Synchronous Search-Return State *(recently fixed)*
`NavState` now holds `returnToId: String` (non-nullable) instead of the old nullable `previousSelectedId`. It is written **synchronously** inside `onSearchExpand` at the exact moment the user opens search, capturing the correct current item with zero async race:

```kotlin
onSearchExpand = {
    searchItem?.let { item ->
        state = state.copy(isSearchExpanded = true, returnToId = state.selectedId)
        onItemSelected(item)
    }
}
```

`LaunchedEffect` is no longer responsible for tracking the return target, eliminating the dual-write race that broke multi-open/close cycles.

### 4. `rememberSaveable` with Custom `NavStateSaver`
Internal state survives configuration changes and process death. The saver serialises all four fields (`selectedId`, `returnToId`, `searchQuery`, `isSearchExpanded`) to a plain `Map<String, Any?>` and has a graceful fallback for `returnToId` when restoring old state bundles.

### 5. Three Distinct Indicator Styles
`NavIndicator` is a sealed class with `Ripple`, `Dot`, and `Line`. Each variant has independent size/padding parameters. `StandardNavContainer` resolves `Color.Unspecified` (the default) to the appropriate `NavColor` value at render time, so indicators work out-of-the-box without a single color argument.

```kotlin
val indicatorColor = when (val ind = config.navIndicator) {
    is NavIndicator.Ripple -> if (ind.color == Color.Unspecified) config.navColor.selectedRippleColor else ind.color
    is NavIndicator.Dot    -> if (ind.color == Color.Unspecified) config.navColor.selectedIconColor  else ind.color
    is NavIndicator.Line   -> if (ind.color == Color.Unspecified) config.navColor.selectedIconColor  else ind.color
}
```

### 6. Sensible Color Defaults *(recently fixed)*
`NavColor.borderColor` is now `Color(0xFFE0E0E0)` — a neutral Light Grey that works on both light and dark surfaces. The previous `Color.Black` default was unusable without manual override on every project.

### 7. `interpolate()` Dp Utility
A typed, clamped linear range mapper used internally to animate icon padding and item width as the search bar transitions height. Clean and reusable.

```kotlin
internal fun interpolate(value: Dp, inputMin: Dp, inputMax: Dp, outputMin: Dp, outputMax: Dp): Dp
```

### 8. Input Validation with Descriptive Messages
Three `require()` guards at the top of `BottomNavigation`:
- Items list is not empty
- All item IDs are unique (duplicate check added recently)
- The provided `selectedId` exists in the list

### 9. Crossfade Icon Animation
`NavItemView` uses `Crossfade(targetState = isSelected)` so icon transitions between outlined → filled variants are smooth rather than instant.

### 10. Label Animation
`AnimatedVisibility` with `fadeIn + expandVertically` / `fadeOut + shrinkVertically` — labels collapse with a proper spatial animation when `hideLabelsOnSearchExpand = true`, not just an opacity cut.

### 11. Haze Blur with Null Degradation
`BlurExt.backgroundBlur()` is a no-op when `hazeState == null`. The consumer can omit blur entirely and the bar falls back to flat solid color — no crash, no API change required.

### 12. `BadgeData` 99+ Truncation
Numeric badges cap at `"99+"` automatically. `showDot` and `count` are independent, allowing flexible badge configurations.

### 13. IME Padding on Search Close Button
The "✕" dismiss button in `SearchSection` has its padding driven by `WindowInsets.ime`, so it slides away from behind the keyboard correctly.

---

## ⚠️ What's Still Problematic

### 🔴 P1 — `material-icons-extended` in `commonMain`

**File:** `flow-tab/build.gradle.kts` line 71

```kotlin
implementation(libs.material.icons.extended) // ← ~15 MB to every consumer's binary
```

The only icon from the extended set used inside the library is `Icons.Default.Close` in `SearchSection.kt`. The base `material-icons` (or a bundled inline vector) would cover this. Every app that depends on FlowTab-CMP currently pays a ~15 MB size penalty even if they don't need extended icons themselves.

**Fix:** Remove `material.icons.extended`. Replace `Icons.Default.Close` with a hardcoded `ImageVector.Builder` path, or expose a `dismissIcon: ImageVector` parameter in `NavConfig` so consumers supply their own.

---

### 🔴 P2 — `Search + Isolated` Co-existence Overlap

**File:** `BottomNavContent.kt` lines 100–107 and 141–153

Both `SearchSection` and `IsolatedSection` are anchored to `Alignment.BottomEnd`. If a consumer declares both a `NavItemType.Search` item and a `NavItemType.Isolated` item simultaneously, they render on top of each other. The standard container's width calculation only subtracts one element slot (`containerWidthDp - searchWidth - 12.dp`).

**Fix:** Detect both being present and offset one:

```kotlin
.width(
    when {
        searchItem != null && isolatedItem != null ->
            containerWidthDp - searchWidth - config.height - 20.dp
        searchItem != null || isolatedItem != null ->
            containerWidthDp - searchWidth - 12.dp
        else -> containerWidthDp
    }
)
// IsolatedSection: add end offset = searchWidth + 12.dp when searchItem != null
```

---

### 🟡 P3 — Triple Background Layering Breaks Non-Blur Mode

**File:** `StandardNavContainer.kt` lines 88–100 (same pattern in `SearchSection.kt`, `IsolatedSection.kt`)

```kotlin
.background(color = backgroundColor.copy(if (enableBlur && hazeState != null) 1f else 0.5f))
.backgroundBlur(config, hazeState)  // no-op when hazeState == null
.background(color = backgroundColor.copy(blurIntensity))  // always runs (default 0.95f)
```

When blur is disabled, both the first (α=0.5) and third (α=0.95) layers fire, composing to an unintended near-opaque muddy result rather than a clean solid background.

**Fix:** Collapse to a single conditional:

```kotlin
.background(
    color = if (config.enableBlur && hazeState != null)
        config.navColor.backgroundColor.copy(config.blurIntensity)
    else
        config.navColor.backgroundColor
)
.backgroundBlur(config, hazeState)
```

---

### 🟡 P4 — `IsolatedSection` Ignores `iconsSize` and Has No Selected State

**File:** `IsolatedSection.kt` lines 77–83

```kotlin
Icon(
    modifier = Modifier.size(24.dp),             // ← hardcoded, ignores config.iconsSize
    tint = config.navColor.unSelectedIconColor   // ← always unselected
)
```

The icon size bypasses `NavConfig.iconsSize`, producing visual inconsistency when the consumer customises icon sizing. The tint is permanently `unSelectedIconColor` — while a FAB-style button often shouldn't show selection, this is surprising and undocumented.

**Fix:**
```kotlin
Icon(
    modifier = Modifier.size(config.iconsSize),
    tint = config.navColor.unSelectedIconColor  // document as intentional, or add isolatedIconColor to NavColor
)
```

---

### 🟡 P5 — Tapped Item Discarded When Search Is Expanded

**File:** `BottomNavContent.kt` lines 115–122

```kotlin
onItemClick = { item ->
    if (isSearchExpanded) {
        onSearchCollapse()  // ← `item` is completely dropped here
    } else {
        onItemClick(item)
    }
}
```

If the user taps "Profile" while search is open, FlowTab always collapses search and returns to `returnToId` (whatever was selected before search opened), ignoring the tapped destination. This is surprising for users who deliberately tap a different item.

**Fix — Option A:** Pass the tapped item to `onSearchCollapse`:
```kotlin
// Signature: onSearchCollapse: (targetItem: NavItem?) -> Unit
onSearchCollapse = { targetItem ->
    val returnItem = targetItem ?: items.find { it.id == state.returnToId } ?: standardItems.firstOrNull()
    state = state.copy(isSearchExpanded = false, searchQuery = "")
    returnItem?.let { onItemSelected(it) }
}
```

**Fix — Option B:** Add `NavConfig.navigateOnSearchCollapseTap: Boolean = false` and let the consumer decide the UX.

---

### 🟠 P6 — `BadgeData(count = 0)` Renders an Empty Circle

**File:** `Badge.kt` lines 29–49

```kotlin
if (badge.showDot || (badge.count != null && badge.count > 0)) {
    Box(/* draws 16dp circle */) {
        badge.count?.let { count ->
            if (count > 0) { Text(...) }       // text skipped, but circle still painted
        }
    }
}
```

A consumer who sets `BadgeData(count = 0)` to clear notifications gets a blank red dot rendered on the icon.

**Fix (one character):**
```kotlin
if (badge.showDot || (badge.count != null && badge.count > 0)) {
```

---

### 🟠 P7 — Hardcoded `"Search..."` Placeholder

**File:** `SearchSection.kt` line 162

```kotlin
Text(text = "Search...", ...)
```

Not localizable, not customizable. Consumers targeting non-English markets cannot change this without forking.

**Fix:** Add to `NavConfig`:
```kotlin
val searchPlaceholder: String = "Search..."
```

---

### 🔵 P8 — Dead `@OptIn(ExperimentalComposeUiApi::class)` Annotation

**File:** `BottomNavigation.kt` line 60

No `@ExperimentalComposeUiApi`-annotated API is used inside `BottomNavigation`. This is a leftover from a previous implementation. It falsely signals to consumers that this composable touches an experimental surface that could change without notice.

**Fix:** Remove the annotation.

---

### 🔵 P9 — `require()` Guards in Composable Scope

**File:** `BottomNavigation.kt` lines 75–77

These `require()` calls execute on **every recomposition**. A consumer who momentarily passes an inconsistent `selectedId` (e.g., during a NavHost transition before the destination has settled) gets a hard crash rather than a graceful fallback.

**Fix:** Guard only the structural invariants (non-empty, unique IDs) strictly. Replace the `selectedId` existence check with a warning log in debug or a coerced fallback:
```kotlin
// Instead of crashing, fall back gracefully
val safeSelectedId = if (items.any { it.id == selectedId }) selectedId else items.first().id
```

---

### 🔵 P10 — Missing `iosX64` Target

**File:** `flow-tab/build.gradle.kts` lines 56–57

```kotlin
iosArm64 { binaries.framework { baseName = "FlowTabKit" } }
iosSimulatorArm64 { binaries.framework { baseName = "FlowTabKit" } }
// ← iosX64 missing; Intel Mac simulators cannot build
```

**Fix:**
```kotlin
iosX64 { binaries.framework { baseName = "FlowTabKit" } }
```

---

## 🚀 Priority Summary

| # | Issue | Severity | Effort | Status |
|---|-------|----------|--------|--------|
| P1 | `material-icons-extended` binary bloat | 🔴 High | Low | ✅ Fixed |
| P2 | Search + Isolated overlap | 🔴 High | Medium | ✅ Fixed |
| P3 | Triple background / broken non-blur mode | 🟡 Medium | Low | Open |
| P4 | `IsolatedSection` ignores `iconsSize`, no selection tint | 🟡 Medium | Low | ✅ Fixed (size) |
| P5 | Tapped item discarded on search collapse | 🟡 Medium | Low | Open |
| P6 | `count = 0` badge renders empty circle | 🟠 Low | Trivial | ✅ Fixed |
| P7 | Hardcoded `"Search..."` placeholder | 🟠 Low | Low | ✅ Fixed |
| P8 | Dead `@OptIn` annotation | 🔵 Cleanup | Trivial | ✅ Fixed |
| P9 | `require()` hard-crashes in composable scope | 🔵 Cleanup | Low | ✅ Fixed |
| P10 | Missing `iosX64` target | 🔵 Cleanup | Trivial | Open |

---

## 📝 Recently Fixed (this session)

| Was | Now |
|-----|-----|
| `previousSelectedId: String?` — nullable, set asynchronously in `LaunchedEffect`; wrong icon highlighted after multiple search open/close cycles | `returnToId: String` — non-nullable, captured synchronously in `onSearchExpand`; zero timing race |
| `LaunchedEffect` set `previousSelectedId = state.selectedId`, racing against the synchronous `state.copy(selectedId = item.id)` in `onItemClick` | `LaunchedEffect` only syncs `selectedId` and `isSearchExpanded`; does not touch `returnToId` |
| `onSearchCollapse` read `state.previousSelectedId` *after* mutating state (stale read risk) | `returnToId` is read *before* the `state.copy()` call — always consistent |
| `NavColor.borderColor` defaulted to `Color.Black` — unusable without override | Defaults to `Color(0xFFE0E0E0)` Light Grey — works on any background |
| All three `NavIndicator` variants defaulted `color` to `Color.Red` | All default to `Color.Unspecified`; `StandardNavContainer` auto-resolves to matching `NavColor` |
| `NavIndicator.Ripple.color` was declared but ignored in rendering | Now properly read and applied with the full `Color.Unspecified` sentinel path |
| No duplicate ID validation | `require(items.distinctBy { it.id }.size == items.size)` added |
