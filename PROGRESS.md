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
Step 13 complete: `:tick` emits one `:food-gathered` event per forager. Next (step 14):
`terrain-at` looks up a tile in `{:tiles {[x y] :water ...}}`, defaulting to `:grass`
(start of terrain, MVP item 3). Failing test in `grid_test.clj`.

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

## Open questions / things to revisit
- Consider whether unknown events should be ignored or reported as errors.
