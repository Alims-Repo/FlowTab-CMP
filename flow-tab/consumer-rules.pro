##──────────────────────────────────────────────────────────────────────────────
## FlowTab-CMP — consumer ProGuard / R8 rules
##
## This file is bundled inside the AAR and merged automatically into the
## consuming app's R8 configuration at build time.
##
## Only the public API is kept. Internal classes (NavState, NavStateSaver,
## all ui.* / util.* internals) are intentionally absent — R8 is free to
## shrink and obfuscate them.
##──────────────────────────────────────────────────────────────────────────────


# ── 1. Public entry-point composable ──────────────────────────────────────────
# BottomNavigation() is a top-level @Composable function.
# The Kotlin compiler emits it into a class named after the source file.
-keep class io.github.alimsrepo.flowtab.BottomNavigationKt { *; }


# ── 2. Public data classes ────────────────────────────────────────────────────
# `{ *; }` retains all fields, all methods (including copy(), copy$default(),
# equals(), hashCode(), toString(), componentN()) so consumer code that uses
# named-argument copies or destructuring works after minification.

-keep class io.github.alimsrepo.flowtab.domain.model.NavItem   { *; }
-keep class io.github.alimsrepo.flowtab.domain.model.NavConfig { *; }
-keep class io.github.alimsrepo.flowtab.domain.model.NavColor  { *; }
-keep class io.github.alimsrepo.flowtab.domain.model.BadgeData { *; }


# ── 3. NavItemType sealed class and its subclasses ────────────────────────────
# Standard and Search are data objects; Isolated is a data class.
# $* covers one level of nesting (Standard, Search, Isolated).
-keep class io.github.alimsrepo.flowtab.domain.model.NavItemType    { *; }
-keep class io.github.alimsrepo.flowtab.domain.model.NavItemType$*  { *; }


# ── 4. NavIndicator sealed class and its subclasses ───────────────────────────
# Ripple, Dot, and Line are data classes with user-facing constructor parameters.
-keep class io.github.alimsrepo.flowtab.domain.model.NavIndicator    { *; }
-keep class io.github.alimsrepo.flowtab.domain.model.NavIndicator$*  { *; }


# ── 5. Kotlin metadata ────────────────────────────────────────────────────────
# @kotlin.Metadata is required for:
#   • Kotlin reflection (default parameter handling, named args)
#   • sealed-class when-exhaustiveness at runtime
#   • @Stable / @Immutable annotation retention read by the Compose runtime
-keep class kotlin.Metadata { *; }
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod


# ── 6. Compose stability annotations ─────────────────────────────────────────
# @Immutable and @Stable on data classes must survive shrinking so the
# Compose compiler's generated stability checks remain correct.
-keepclassmembers @androidx.compose.runtime.Immutable class * { *; }
-keepclassmembers @androidx.compose.runtime.Stable   class * { *; }


# ── 7. Suppress known harmless warnings ──────────────────────────────────────
-dontwarn java.lang.invoke.StringConcatFactory
-dontwarn dev.chrisbanes.haze.**

