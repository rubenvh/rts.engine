# Progress log

Game vision lives in `DESIGN.md` (14000 BC hunter/gatherer clan on the Great Plains).
Design session (2026-11-18) fleshed it out: shared-clan local co-op (parent + 8yo daughter),
soft tone (no deaths — scared/exhausted/recovered), seasons as heartbeat, individuals who
reproduce, tech tree as the painted cave wall, poetic scenarios layered on a sandbox.
Co-op input decided: mouse+keyboard (P1) + gamepad (P2), one shared screen, two colored
cursors; core stays player-agnostic (N command producers, one queue).
Trails added: emergent desire paths from movement wear + deliberate craftsman paths;
unused trails decay back to grass each tick ("the land remembers").
Cave home-building added: functional/cozy/beautiful improvements; coziness drives
recovery, winter survival and baby survival; possible cave interior view.
Persons, not units: names, decoration/dress-up (craftsman adornments), aging stages,
personality as a fold over each person's experienced events, bonds from shared experiences.
MVP milestone = roadmap items 2–4 (tick, terrain, read-only Quil window); a "Someday"
list in DESIGN.md parks scope (meaning of losing, cave interior view, first dog).
Art direction: 2D pixel art, cozy warm palette, fire must feel warm, visible seasons/weather.
World generation: seeded, realistic geography (height → rivers → biomes), roadmap item 6;
MVP item 3 stays a hand-placed test map.
DESIGN.md now holds the design-driven roadmap; follow it for next steps.

## Current step
Step 34 complete: mouse input, first interactive feature. Pure helpers `pixel->tile`
(conversion-naming `foo->bar`), domain query `unit-at` (returns the *id* — ids are
references, values are snapshots), `on-click` selects/deselects via `[:ui :selected]`.
Sketch state reshaped to `{:game <domain> :ui <viewer>}` — selection is UI state,
never domain (replay must not depend on attention). Tick decoupled from frame rate:
30 fps render, `:tick` every 30th frame (`q/frame-count` + `mod`); `:update` must
always return a state. `draw-on-tile` is a user-designed higher-order helper
(`ellipse-mode :corner` dissolved its boolean flag).

Step 35 complete: right-click issues `:move-unit` for the selected unit — the first
human-issued command, same `decide`/`handle` path as the clock. `if-let` for
"selected or bail"; rejected commands (`:too-far`, water, …) silently keep the old
state (error branch reserved for future feedback). `on-click` branches return
nil-or-new-state, collapsed by a single `(or … app-state)`. Moves are one adjacent
tile; pathfinding later. Next candidates: multi-tile movement over ticks
(pathfinding), feedback for rejected commands, trails, or world generation.
Economy note: one forager feeds `units-fed-per-forager` (3) people in spring.

Design session (2026-11-22) reshaped the roadmap in DESIGN.md: mouse input pulled
before trails (trails need movement); individuals pushed later; new item 7 = first
art pass (hand-drawn canonical pixel tiles + tint/overlay for seasons, AI variants
maybe); camera/scrolling folded into world gen (item 6). Decisions recorded in
DESIGN.md: seasonal look = canonical tile + tint/overlay; seasons change terrain
*properties* (ice, floods, fords, snow move-cost) as data; passability as
terrain-requires × traveler-capabilities (Someday); atmosphere layer parked.

## Completed steps
- Step 1: project setup; `clojure -X:test` passes.
- Step 2: `evolve` applies a `:unit-spawned` event.
- Step 3: `evolve` selects behavior by event type, handles unit movement, and replays events with `reduce`.
- Step 4: `decide` translates move commands and returns errors for missing units and unsupported command types.
- Step 5: `in-bounds?` checks grid coordinates, and `decide` rejects out-of-bounds moves.
- Step 6: `decide` handles `:spawn-unit`, rejecting taken unit ids and out-of-bounds positions.
- Step 7: `evolve` handles `:food-gathered`, incrementing `[:resources :food]` with `update-in` and `(fnil + 0)`.
- Step 8: `decide` handles `:forage`, rejecting non-foragers with `get-in`.
- Step 9: refactored `decide` with `let` and `{:keys [...]}` destructuring, on green tests.
- Step 10: `handle` composes `decide` and `reduce evolve`, passing errors through.
- Step 11: `adjacent?` checks Chebyshev distance with vector destructuring and `Math/abs`.
- Step 12: `:move-unit` rejects moves farther than one tile (`:too-far`), guard ordered after `:unit-not-found`.
- Step 13: `:tick` maps over foragers (`vals`, `filter`, `mapv`, `#(...)` lambda) to emit `:food-gathered` events.
- Step 14: `terrain-at` looks up sparse `:tiles` keyed by position vectors, defaulting to `:grass`.
- Step 15: `:move-unit` rejects `:water` destinations; ordered `cond` guards let later checks assume earlier ones.
- Step 16: `coords` enumerates grid positions with a `for` comprehension (outer binding varies slowest).
- Step 17: `parse-map` parses ASCII rows via a `def`'d legend, `for` with `:let`/`:when`, and `into {}`.
- Step 18: `:forage` requires `:forest` terrain; cleanup with `not=` and a single `unit` binding.
- Step 19: `require-unit` helper + `(or error happy-path)` removes duplicated guards.
- Step 20: `:tick` filters on `and` of type + terrain, reusing `terrain-at`.
- Step 21: hand-drawn ASCII world as `def` data in `rts.domain.world`; new test namespaces must be added to `test-runner`.
- Step 22: pure `terrain-color` palette in the render adapter; color is presentation, not domain.
- Step 23: `draw-world` with Quil (`doseq`, `q/fill`/`q/rect`/`q/ellipse`, `no-stroke`); run with `clojure -M:render`.
- Step 24: game loop with fun-mode middleware; `:update` = `(:state (handle state {:cmd :tick}))`; MVP window now ticks.
- Step 25: status bar strip below the map; window height = map + bar; `q/text` y is the baseline.
- Step 26: `:time-passed` event increments `[:time :tick]`; `into [x] xs` prepends; time is a recorded fact.
- Step 27: `rts.domain.time` — `day-of`/`season-of` derive the calendar from ticks with `quot`/`mod`.
- Step 28: pure `status-text` (food, day, season) in the render adapter; `draw-bar` just paints it.
- Step 29: `season-progress` — exact ratio of season elapsed, from raw ticks (fluid season transitions decided).
- Step 30: `blend` lerps two RGB colors by progress — multi-collection `mapv`, closure over `ratio`.
- Step 31: seasonal `palette` as data; `terrain-color` blends season→next with a mid-season-peak shifted clock.
- Step 32: seasonal `yields` as data; per-forager amounts, zero-amount events dropped; `->>` pipeline + `comp`.
- Step 33: `:food-consumed` (clamped at 0) on day completion; `day-passed?`; `rts.domain.economy` extracted.
- Step 34: mouse selection — `pixel->tile`, `unit-at`, `on-click`, `{:game … :ui …}` app state, 30 fps with 1 Hz tick.
- Step 35: right-click `:move-unit` — first human command through `handle`; `(or branches app-state)` fallback.

## Concepts learned
- A Clojure CLI alias can run `clojure.test` with `clojure -X:test`.
- Maps, keyword lookup, and immutable updates with `assoc` and `assoc-in`.
- `case` selects an expression based on a value, with an optional default.
- `cond` checks conditions in order and returns the result for the first true condition.
- `reduce` passes a function value to repeatedly process events, carrying each result forward.
- Commands express intent; `decide` can turn an accepted command into an event fact.
- `contains?` checks whether a map has a key, and `if` selects a value based on that check.
- Decision errors are results, not game-state events.
- Grid coordinates use `[x y]`; valid values start at zero and are below width and height.
- `first` and `second` retrieve x and y from a position vector.
- `case` clauses are alternating value/expression pairs; a missing expression silently steals the default.
- `update-in` transforms a nested value with a function; `fnil` substitutes a default for `nil` arguments.
- `get-in` reads a nested value by path; sibling of `assoc-in`/`update-in`.
- `let` with `{:keys [...]}` destructures maps, like C# tuple deconstruction or property patterns.
- An error result short-circuits; a falsy keyword lookup (`(:error result)`) distinguishes result shapes.
- Vectors destructure positionally (`[x y]`), in `let` and in parameter lists.
- Java interop with static methods: `(Math/abs n)`; prefer core functions (`max`) when they exist.
- Anonymous functions: `(fn [x] ...)` and the `#(... %)` shorthand are Clojure's lambdas.
- `(get m k default)` and `(get-in m path default)` return a fallback when the key is missing; vectors can be map keys.
- `for` is a lazy comprehension (~ LINQ nested `from`); `=` compares seqs and vectors element-wise; `vec` converts.
- `for` supports `:let` and `:when` modifiers; maps are functions: `(legend ch :grass)` looks up with a default.
- `map` the function transforms sequences (LINQ `Select`); it is not map lookup — that's `get` or calling the map.
- `or` returns the first truthy value (like C# `??`); an error map is truthy, `nil` falls through.
- `when-not` returns its body or `nil` — ideal for "error or nothing" checks. `(get m k)` is nil-safe; `(m k)` throws if `m` is nil.
- Sets are functions: `(#{:water} x)` returns `x` or `nil`; `some` returns the first truthy result (LINQ `Any` + witness).
- Lenient parsing (defaults) hides typos; strict parsing (throwing) surfaces them — a trade-off to choose deliberately.
- `doseq` is the eager, side-effecting `for` (same `:let`/`:when` modifiers); iterating a map yields `[k v]` pairs — use `vals` for values.
- Quil: pixel origin top-left, zero-based; shapes stroke by default (`no-stroke`); `ellipse` takes its *center*.
- Quil fun-mode: `:setup` *returns* the state, `:update` is state→state, `:draw` receives it — pure core, I/O at the edge.
- Reading stack traces: find your own namespace's deepest frame; `(keyword map)` beats `(map keyword)` for nil-safety.
- `quot`/`mod` for integer division and cycling; store one fact, derive many projections (tick → day → season).
- `name` strips the colon from a keyword; `format` ~ C# `string.Format`; `inc`/`dec` return new values.
- Zero-based counting in the domain (clean `quot`/`mod`), one-based at the presentation edge (`inc`).
- Integer `/` yields exact ratios; `==` compares numeric value across types. Keep domain math exact; lose precision only at the edges.
- `map`/`mapv` accept multiple collections (LINQ `Zip`); anonymous fns are closures — they capture surrounding bindings.
- Shift the clock, not the data (coordinate transform); `mod` wraps negatives correctly where `quot` rounds toward zero.
- `->>` threads a value through last-argument positions (~ F# `|>`); `->` threads first (for `assoc`-style map fns); `comp` composes functions.
- Events are facts worth recording: emit no event rather than a zero-amount one; keeps the replay log meaningful.
- `fnil` decorates any function with nil-safety; `(seq coll)` is the idiomatic non-empty test (~ LINQ `Any()`).
- When a pipeline's shape diverges, name the parts in a `let`; `->>` for seq transforms, `->` for building values.
- Store ids to remember, values to use now — immutable values are snapshots, ids are stable references.
- Maps-are-functions cuts both ways: passing a map where a fn is expected fails silently with nils.
- `when-let` binds and tests at once; frame handlers must be total (always return state, never nil).
- Functions are values: pass `q/rect`/`q/ellipse` into your own higher-order helpers; beware the boolean-trap parameter.
- `if-let` = `when-let` with an else; nil-returning `when` branches + one `(or … fallback)` replace repeated else clauses.
- Commands-as-data: clock, mouse, and future AI all speak to the same `decide` — input is an adapter concern.

## Open questions / things to revisit
- Consider whether unknown events should be ignored or reported as errors.
- Camera/scrolling: UI-only state in the render adapter (`{:game ..., :camera ...}`),
  never in the domain — replay must not depend on viewpoint. Build it with world
  generation (roadmap item 6), when maps outgrow the window. Arrow keys via `:key-pressed`.
