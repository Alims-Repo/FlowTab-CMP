##──────────────────────────────────────────────────────────────────────────────
## FlowTab-CMP — library build ProGuard / R8 rules
##
## Applied only when building this module with isMinifyEnabled = true.
## (Currently disabled; these rules serve as documentation and a safety net
## if minification is enabled in a future release.)
##
## Rules here mirror consumer-rules.pro so the library is self-consistent.
##──────────────────────────────────────────────────────────────────────────────


# ── Public entry-point composable ─────────────────────────────────────────────
-keep class io.github.alimsrepo.flowtab.BottomNavigationKt { *; }


# ── Public data classes ────────────────────────────────────────────────────────
-keep class io.github.alimsrepo.flowtab.domain.model.NavItem   { *; }
-keep class io.github.alimsrepo.flowtab.domain.model.NavConfig { *; }
-keep class io.github.alimsrepo.flowtab.domain.model.NavColor  { *; }
-keep class io.github.alimsrepo.flowtab.domain.model.BadgeData { *; }


# ── Sealed classes and their subclasses ───────────────────────────────────────
-keep class io.github.alimsrepo.flowtab.domain.model.NavItemType    { *; }
-keep class io.github.alimsrepo.flowtab.domain.model.NavItemType$*  { *; }

-keep class io.github.alimsrepo.flowtab.domain.model.NavIndicator    { *; }
-keep class io.github.alimsrepo.flowtab.domain.model.NavIndicator$*  { *; }


# ── Internal implementation — intentionally NOT kept ──────────────────────────
# NavState, NavStateSaver, all ui.* and util.* classes are internal;
# R8 is free to shrink, merge, and obfuscate them.


# ── Kotlin metadata and attributes ────────────────────────────────────────────
-keep class kotlin.Metadata { *; }
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod


# ── Compose stability annotations ─────────────────────────────────────────────
-keepclassmembers @androidx.compose.runtime.Immutable class * { *; }
-keepclassmembers @androidx.compose.runtime.Stable   class * { *; }


# ── Suppress known harmless warnings ──────────────────────────────────────────
-dontwarn java.lang.invoke.StringConcatFactory
-dontwarn dev.chrisbanes.haze.**

