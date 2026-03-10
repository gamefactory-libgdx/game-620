# Glow Snake – Figma AI Design Brief

---

## 1. Art Style & Color Palette

**Glow Snake** adopts a **neon arcade aesthetic** with a modern flat design foundation and subtle geometric elements. The primary palette consists of deep navy/black backgrounds (#0A0E27) contrasted with vibrant neon accents: electric cyan (#00D9FF), hot magenta (#FF006E), and lime green (#39FF14). Accent colors include soft purple (#9D4EDD) for secondary UI and warm orange (#FF8C42) for power-up highlights. Typography uses a bold, geometric sans-serif (Audiowide or similar) with heavy weight (700–900) for titles and medium weight (600) for body text. All UI elements feature subtle glow effects (blur + color overlay) to reinforce the neon theme, while the snake and grid maintain crisp, high-contrast edges against the dark backdrop.

---

## 2. App Icon — icon_512.png (512×512px)

**Background:** Radial gradient from deep navy (#0A0E27) at edges to dark purple (#1A0033) at center, creating depth.

**Central Symbol:** A coiled glowing snake rendered in 3D isometric perspective, occupying the central 400×400px safe zone. The snake body is composed of 5–6 segments in gradient cyan (#00D9FF) to magenta (#FF006E), with each segment slightly offset to suggest movement. The snake's head features a bold triangular shape pointing upper-right, rendered in bright cyan with a small white highlight on the left edge. A glowing food pellet (small circle, lime green #39FF14) floats to the right of the snake's mouth.

**Glow & Shadow Effects:** 8–12px outer glow around the entire snake using cyan (#00D9FF) at 60% opacity, creating a halo effect. Inner shadow on the background gradient (5px, black at 40% opacity) adds dimensionality. The food pellet has a 4px glow in lime green (#39FF14).

**Overall Mood:** Dynamic, energetic, and instantly recognizable—the icon communicates "retro arcade" and "movement" at a glance.

---

## 3. Backgrounds (480×854 portrait)

**Background List:**
- backgrounds/bg_main.png
- backgrounds/bg_grid.png

---

### backgrounds/bg_main.png

The main menu backdrop features a deep navy base (#0A0E27) with an animated-static pattern of diagonal neon lines in cyan (#00D9FF) and magenta (#FF006E) at 15% opacity, creating a subtle grid texture. A soft radial glow emanates from the center (purple #9D4EDD at 20% opacity, 300px radius), providing subtle depth. In the lower half, a faint horizontal scanline pattern (thin white lines, 2px apart, 5% opacity) evokes classic arcade monitors. No content or UI elements occupy the background itself—it is purely atmospheric and supports the main menu title and buttons.

---

### backgrounds/bg_grid.png

The gameplay background shows a dark charcoal (#1A1A2E) canvas with a faint grid overlay (cyan #00D9FF lines at 8% opacity, 1px width) that extends edge-to-edge but remains subordinate to the active game grid. A subtle bottom-left to top-right gradient overlay (from black #000000 to purple #2A1A4A, 20% opacity) adds depth without distraction. Occasional geometric accents—small neon dots or faint triangles in lime green (#39FF14) at 10% opacity—scatter across the background at irregular intervals, reinforcing the neon aesthetic without interfering with gameplay.

---

## 4. UI Screens (480×854 portrait)

---

### MainMenuScreen

**Background:** bg_main.png  
**Header:** Title "GLOW SNAKE" centered at top in bold cyan (#00D9FF), 64px Audiowide 900 weight, with a 6px magenta (#FF006E) text glow beneath. Subtitle "ARCADE" in smaller lime green (#39FF14), 20px, 4px below title.

**Buttons:** Four primary buttons stacked vertically in center-right area—"PLAY" (cyan background, magenta text), "LEADERBOARD" (magenta background, cyan text), "HOW TO PLAY" (lime green background, dark navy text), "SETTINGS" (soft purple background, white text). Each button is 240px wide, 56px tall, 16px rounded corners, with 20px spacing between. All buttons feature subtle outer glow (3px, 40% opacity) matching their background color.

**Key Elements:** Small glowing snake animation (3–4 segments) loops in the top-right corner, traveling across the header area to showcase the mechanic. Copyright/version text ("v1.0") in small white, bottom-right, 10px.

---

### ModeSelectScreen

**Background:** bg_main.png  
**Header:** "SELECT MODE" in bold cyan (#00D9FF), 48px Audiowide 900, centered at top.

**Buttons:** Two large mode-selection cards, side-by-side or vertically stacked:
- **CLASSIC MODE** card: Navy background (#0A0E27) with cyan border (3px), centered title "CLASSIC" in cyan, subtitle "Walls Kill" in smaller magenta text. 220px wide, 140px tall, 12px rounded corners. Includes small icon (geometric wall symbol) in lime green.
- **WRAP MODE** card: Same dimensions and styling, title "WRAP" in magenta, subtitle "Walls Loop" in cyan. Icon shows a looping arrow in lime green.

Both cards positioned center, 24px vertical spacing. Each card features a hover/interactive glow (4px outer glow, matching text color).

**Navigation:** "BACK" button (text only, cyan, small, 12px font) positioned bottom-left.

---

### DifficultySelectScreen

**Background:** bg_main.png  
**Header:** "SELECT DIFFICULTY" in cyan (#00D9FF), 48px Audiowide 900, centered at top.

**Buttons:** Three difficulty cards arranged horizontally or vertically:
- **SMALL:** "SMALL GRID" label, lime green (#39FF14) text, 180px wide, 100px tall. Shows "16×16" subtitle in white, 12px.
- **MEDIUM:** "MEDIUM GRID" label, cyan (#00D9FF) text. Shows "24×24" subtitle. Same dimensions.
- **LARGE:** "LARGE GRID" label, magenta (#FF006E) text. Shows "32×32" subtitle. Same dimensions.

All cards have navy backgrounds (#0A0E27) with colored borders (2px, matching label color), 8px rounded corners, 16px spacing. Each card has a subtle glow matching its accent color (3px outer glow, 40% opacity).

**Navigation:** "BACK" button (text only, cyan, small, 12px font) bottom-left.

---

### ClassicSmallScreen

**Background:** bg_grid.png  
**HUD (Game View):** Score counter top-left ("SCORE: 0", cyan #00D9FF, 18px Audiowide 600), lives/health counter top-right (heart icon + number, magenta text, 18px). Timer or turn counter (if applicable) centered top in smaller text (12px, lime green #39FF14).

**Game Grid:** 16×16 grid rendered centrally, occupying ~320×320px, with cyan (#00D9FF) cell borders (1px), dark charcoal cells (#1A1A2E). Snake segments rendered as bright cyan/magenta gradient blocks with subtle glow. Food pellets are lime green (#39FF14) circles with 2px glow. Power-ups (speed boost = orange #FF8C42, ghost pass = white with purple aura) are clearly distinct.

**Bottom HUD:** Pause button (text only, cyan) centered bottom. Optional instruction text ("SWIPE TO MOVE") in small white, 10px, 8px above pause button.

**No title or header on this screen**—gameplay is the focus.

---

### ClassicMediumScreen

**Background:** bg_grid.png  
**HUD (Game View):** Identical layout to ClassicSmallScreen. Score counter top-left (cyan, 18px), lives counter top-right (magenta, 18px), timer top-center (lime green, 12px).

**Game Grid:** 24×24 grid rendered centrally, occupying ~360×360px (larger than Small), with same cyan borders (1px) and styling. All game elements (snake, food, power-ups) scale proportionally and maintain the same visual hierarchy.

**Bottom HUD:** Pause button (text only, cyan) centered bottom. Swipe instruction text (small white, 10px) 8px above.

---

### ClassicLargeScreen

**Background:** bg_grid.png  
**HUD (Game View):** Identical HUD layout. Score top-left (cyan, 18px), lives top-right (magenta, 18px), timer top-center (lime green, 12px).

**Game Grid:** 32×32 grid rendered centrally, occupying ~400×400px (maximum playable area while leaving HUD space). Cyan borders (1px), dark charcoal cells (#1A1A2E). Snake, food, and power-ups scale accordingly.

**Bottom HUD:** Pause button (text only, cyan) centered bottom. Swipe instruction text (small white, 10px) 8px above.

---

### WrapSmallScreen

**Background:** bg_grid.png  
**HUD (Game View):** Identical to ClassicSmallScreen. Score top-left (cyan, 18px), lives top-right (magenta, 18px), timer top-center (lime green, 12px). Optional visual indicator ("WRAP MODE" small label, magenta, 12px) near score to distinguish mode.

**Game Grid:** 16×16 grid, same styling. Visually identical to Classic, but behavior differs (no wall collision deaths, walls wrap).

**Bottom HUD:** Pause button (cyan, text only) centered bottom. Swipe instruction (small white, 10px) 8px above.

---

### WrapMediumScreen

**Background:** bg_grid.png  
**HUD (Game View):** Score top-left (cyan, 18px), lives top-right (magenta, 18px), timer top-center (lime green, 12px). "WRAP MODE" label (magenta, 12px) near score.

**Game Grid:** 24×24 grid, same cyan borders and styling.

**Bottom HUD:** Pause button (cyan, text only) centered bottom. Swipe instruction (small white, 10px) 8px above.

---

### WrapLargeScreen

**Background:** bg_grid.png  
**HUD (Game View):** Score top-left (cyan, 18px), lives top-right (magenta, 18px), timer top-center (lime green, 12px). "WRAP MODE" label (magenta, 12px) near score.

**Game Grid:** 32×32 grid, same styling.

**Bottom HUD:** Pause button (cyan, text only) centered bottom. Swipe instruction (small white, 10px) 8px above.

---

### GameOverScreen

**Background:** bg_main.png  
**Header:** "GAME OVER" in large magenta (#FF006E) text, 56px Audiowide 900, centered near top. 6px cyan glow beneath.

**Score Display:** Final score prominently displayed below header in cyan (#00D9FF), 48px Audiowide 700, centered. Subtitle "FINAL SCORE" in smaller white, 14px, 4px above score.

**Stats Box:** Secondary stats (grid size, mode, survival time) displayed in a bordered box (magenta border 2px, navy background #0A0E27, 200px wide, 100px tall) centered 24px below score. Text in white, 12px, line-spaced 16px.

**Buttons:** Three buttons aligned horizontally or stacked vertically below stats:
- **RESTART** (lime green background, dark navy text, 200px wide, 48px tall)
- **MENU** (cyan background, dark navy text, 200px wide, 48px tall)
- **LEADERBOARD** (magenta background, white text, 200px wide, 48px tall) — appears only if score qualifies.

All buttons 12px rounded corners, 12px spacing, subtle glow (3px, 40% opacity) matching background color.

**Navigation:** No back button needed—buttons provide navigation path.

---

### LeaderboardScreen

**Background:** bg_main.png  
**Header:** "LEADERBOARD" in bold cyan (#00D9FF), 48px Audiowide 900, centered at top. Mode/difficulty filter text below (e.g., "CLASSIC — MEDIUM") in smaller magenta text, 16px.

**Leaderboard List:** Scrollable list occupying center area (380px wide, ~600px tall). Each entry row (380px wide, 48px tall) displays:
- **Rank** (left-aligned, cyan, 14px Audiowide 600) — "1.", "2.", etc.
- **Name** (left-aligned, white, 14px) — 4px right of rank
- **Score** (right-aligned, lime green #39FF14, 14px Audiowide 700)
Rows alternate subtle background colors (none/8% white overlay) for readability. Top 3 ranks highlighted with colored left border (1st = gold/orange #FFD700, 2nd = silver #C0C0C0, 3rd = bronze #CD7F32).

**Filter Controls:** Small buttons or tabs above list (12px tall, centered below header) allowing filter by mode and difficulty. Buttons labeled "CLASSIC / WRAP" (left), "SMALL / MEDIUM / LARGE" (center), styled in cyan text, small.

**Navigation:** "BACK" button (text only, cyan, 12px) bottom-left. "MENU" button (text only, cyan, 12px) bottom-right.

---

### SettingsScreen

**Background:** bg_main.png  
**Header:** "SETTINGS" in bold cyan (#00D9FF), 48px Audiowide 900, centered at top.

**Settings List:** Vertically stacked toggle/option rows (360px wide, centered) with 20px vertical spacing:
- **"MUSIC: ON / OFF"** (white text, 16px) with toggle switch (cyan/magenta) on right, 48px wide, 28px tall, 8px rounded corners.
- **"SOUND: ON / OFF"** (white text, 16px) with toggle switch.
- **"VIBRATION: ON / OFF"** (white text, 16px) with toggle switch.
- **"BRIGHTNESS"** (white text, 16px) with slider (cyan background, magenta handle) below, 240px wide.

All toggles feature 2px border (matching the active state color), subtle outer glow, and clear visual feedback on state change.

**Navigation:** "BACK" button (text only, cyan, 12px) bottom-left. "MENU" button (text only, cyan, 12px) bottom-right.

---

### HowToPlayScreen

**Background:** bg_main.png  
**Header:** "HOW TO PLAY" in bold cyan (#00D9FF), 48px Audiowide 900, centered at top.

**Content Panels:** Two or three vertically stacked info sections (360px wide, centered), each with:
- **Section Title** (magenta #FF006E, 18px Audiowide 700)
- **Description Text** (white, 14px, line-height 20px)
- **Visual Illustration** (small diagram or animated GIF preview, 120px × 80px, centered below title)

**Sections:**
1. **"SWIPE TO MOVE"** — Illustration shows hand swipe gesture overlaid on grid. Text explains directional control.
2. **"EAT FOOD"** — Illustration shows snake eating lime green pellet. Text explains food consumption and growth.
3. **"AVOID COLLISIONS"** — Illustration shows snake hitting wall or itself. Text explains Classic (death) vs. Wrap (loop) modes.
4. **"POWER-UPS"** — Illustration showing speed boost (orange) and ghost pass (white/purple) icons. Text explains each briefly.

20px spacing between sections. Each section background is slightly lighter (navy #0A0E27 with 10% white overlay, 8px rounded corners).

**Navigation:** "BACK" button (text only, cyan, 12px) bottom-left. "MENU" button (text only, cyan, 12px) bottom-right.

---

## 5. Export Checklist

- icon_512.png (512×512)
- backgrounds/bg_main.png (480×854)
- backgrounds/bg_grid.png (480×854)
- ui/mainmenu_screen.png (480×854)
- ui/mode_select_screen.png (480×854)
- ui/difficulty_select_screen.png (480×854)
- ui/classic_small_screen.png (480×854)
- ui/classic_medium_screen.png (480×854)
- ui/classic_large_screen.png (480×854)
- ui/wrap_small_screen.png (480×854)
- ui/wrap_medium_screen.png (480×854)
- ui/wrap_large_screen.png (480×854)
- ui/gameover_screen.png (480×854)
- ui/leaderboard_screen.png (480×854)
- ui/settings_screen.png (480×854)
- ui/howtoplay_screen.png (480×854)
- ui/power_up_speed_boost_icon.png (64×64)
- ui/power_up_ghost_pass_icon.png (64×64)

**Total: 18 files**
