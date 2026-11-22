# Game design: 14000 BC

## Vision
A clan of hunter-gatherers in 14000 BC North America has just crossed into the
Great Plains and found a great cave — home. From there the clan explores,
forages, hunts, paints and dreams, season after season, toward the first seed
of agriculture.

**The game is built for local co-op**: a parent and a child (8 years old)
playing *one clan together*. Collaboration is not a mode; it is the point.
The engine, UI and pacing should always favor "we" over "I".

## Pillars
1. **Together.** One shared clan; both players can command any unit.
   Mechanics should invite talking: "you calm the wolves, I'll carry the berries."
2. **Soft world.** Nobody dies on screen. Units get *hurt*, *scared* or
   *exhausted* and return to the cave to recover. Predators are driven off,
   not slaughtered. Rival clans compete for land and herds, not blood.
3. **The rhythm of seasons.** Ticks make days, days make seasons. Summer is
   plenty; winter is the test. The year is the game's heartbeat.
4. **Sandbox first, stories on top.** The base game is free-form survival and
   growth. Scenarios are layered on top as poetic, self-contained chapters.

## The clan
### Unit types
- **Forager** — gathers food, water, clay, wood.
- **Hunter** — hunts herds (food, hides), drives off predators.
- **Shaman** — generates *spirit*; calms scared units; rituals for the seasons.
- **Painter** — turns memories into *knowledge*; paints the clan's story on the
  cave wall (the tech tree is literally the cave wall).
- **Craftsman** — turns wood/stone/hides into tools, clothing, shelter.
- **Child / Elder** — later: children grow into workers; elders hold knowledge.

### Population
Clan members are individuals with a sex and an age. Couples can have babies.
A baby's chance to survive grows with the clan's surplus (food, hides, warmth).
No "training units for 50 food" — the clan grows because it is *cared for*.

### Persons, not units
Every clan member is a *person* with a name, a face and a story.
- **Names**: given at birth (a naming ritual — a spirit/scenario beat).
  Players can rename.
- **Appearance & decoration**: players dress and decorate each person —
  face paint, beads (clay), feathers, hide clothing, hair. Craftsman-made
  adornments make decoration part of the economy. Co-op dress-up is a
  first-class activity.
- **Aging**: baby → child → adult → elder, driven by ticks/seasons. Abilities
  shift with age (children learn by tagging along; elders hold knowledge and
  teach).
- **Personality from lived experience**: each person's character is a fold
  over the events *they* experienced (pure event sourcing!). Surviving a wolf
  scare makes one *brave* or *cautious*; many hunts together make hunters
  *bold*; long winters make *patient* souls. Traits gently affect gameplay
  (a brave hunter resists fear; a patient forager works through winter).
- **Bonds**: people who share experiences grow bonds — friendships, couples
  (ties into reproduction), mentor/child pairs. The clan becomes a web of
  relationships the players know by name.

## Resources
| Resource      | From                      | For                              |
|---------------|---------------------------|----------------------------------|
| Food          | foraging, hunting, crops  | upkeep each tick; surplus = babies survive |
| Water         | rivers, springs           | upkeep; needed near camp sites   |
| Wood          | groves along rivers       | tools, fire, shelter             |
| Stone / flint | outcrops                  | tools, spearheads                |
| Hides         | hunting                   | clothing, warmth in winter       |
| Clay          | river banks               | pottery (storage!), figurines    |
| Spirit        | shaman rituals            | calming, rituals, scenario beats |
| Knowledge     | painter, elders           | unlocks the tech tree            |

## Threats (all soft-toned)
- **Starvation**: each clan member eats every tick; empty stores means the
  clan grows weak (slower, can't have babies) — never on-screen death.
- **Predators**: wolves and sabertooth stalk the edges; they *scare* units,
  who flee to the cave. Hunters and fire drive them off.
- **Winter**: foraging yields drop; stores, hides and fire decide whether the
  clan thrives or merely endures.
- **Rival clans**: AI clans that compete for herds and groves; they can also
  be traded with or, in scenarios, befriended.

## Time & seasons
- The engine `:tick` is the atomic unit of time.
- N ticks = a day; days roll through **spring → summer → autumn → winter**.
- Seasonal modifiers: forage yield, herd movement, predator boldness, water.
- The year-end is a natural narrative beat (the shaman's winter ritual,
  a painting of the year on the wall).

## Progression: the painted wall
The tech tree **is the cave wall**. Knowledge from painters and elders unlocks
new paintings; each painting is a discovery:
fire-keeping → pottery → fishing → drying meat → sowing wild grain →
**the first harvest** (the agricultural village — the sandbox "ending").

## Art direction & atmosphere
- **Old-school 2D pixel art** with hand-made animations; cozy, warm palette.
- **The fire must feel warm**: flicker, glow radius, people gathering around
  it; the emotional center of the cave (later: crackle sound).
- **Seasons and weather are visible**: green springs, golden autumns, snow,
  rain, drifting clouds — weather as atmosphere first, mechanics second.
- Small touches over spectacle: footprints, smoke, fireflies, northern lights
  on rare winter nights.
- Technical: render at a small native resolution and integer-scale up
  (crisp pixels); Quil supports this with an off-screen buffer.
- Seasonal look (decided): one canonical tile per terrain + programmatic
  seasonal tint/overlay (snow, golden leaves) — not a full tileset per season.
- Seasons change terrain *properties*, not just looks (domain, not render):
  ice passable in winter, swamps freeze/thaw, spring floods, late-summer fords,
  deep snow raising move cost, mud season slowing trails. Same data shape:
  season modifies terrain properties.

## The world: generated, but real
Maps are procedurally generated yet geographically believable: mountains feed
rivers, rivers meet the sea at beaches, forests and swamps sit where water
is, grass plains in between, islands offshore.
- Terrain palette: mountains, rivers, lakes, forests, groves, grass fields,
  swamps, beaches, sea, islands, rock outcrops, clay banks, springs.
- Generation is **seeded** — the seed lives in state/events, so replaying an
  event log rebuilds the exact same world (same determinism rule as all
  randomness in the engine).
- Realism heuristics over noise soup: height map → water flow → rivers →
  biomes by moisture and height. The cave spawns somewhere sensible
  (a rocky hill near water and forest).

## The cave: home-building
The cave is not just a spawn point — it is *home*, and making it cozy,
functional and beautiful is a core activity.
- **Functional**: hearth, storage pits and pottery (larger stores, less
  spoilage), drying racks, a tool corner. Built by craftsmen from wood,
  stone, clay and hides.
- **Cozy** (the soft-tone "defense stat"): furs, a windbreak at the entrance,
  a fire kept burning. Coziness speeds recovery of scared/exhausted units,
  carries the clan through winter, and raises baby survival.
- **Beautiful**: the painted wall (the tech tree itself), clay figurines,
  gathered flowers. Beauty generates spirit and lifts the clan's mood;
  some scenarios turn on it.
- **Interior view** (idea): a zoomed-in view of the cave where improvements
  are placed and the painted wall grows over the years — decorating home
  together as a co-op activity.

## Trails: the land remembers
Trails are accumulated history made visible — a natural fit for event sourcing.
- **Emergent (desire paths)**: every `:unit-moved` adds *wear* to the tiles
  crossed. Wear thresholds transform terrain: grass → track → trail → path.
  Nobody builds them; they appear where life happens, and replaying the event
  log regrows them identically.
- **Deliberate**: a craftsman can clear a path (a command costing time and
  effort) — e.g. to reach the far hunting grounds before winter.
- **Decay**: nature takes back what isn't used. Each `:tick`, unwalked tiles
  slowly lose wear; an abandoned path fades to track, then to grass.
- **Effects**: faster movement on trails; scared units flee home *along*
  trails; foragers carry more on a path; snow hides trails in winter;
  rival clans can *follow* your trails to your grounds (risk!).

## Scenarios (poetic chapters on the sandbox)
Each scenario is the sandbox plus a goal, a mood and a short narration.
- **The Long Winter** — the stores are low and the snow came early.
  Keep every clan member warm until the first thaw.
- **The Great Mammoth** — a lone giant crosses the plain. The hunt will feed
  the clan for a season — if hunters, shaman and foragers work as one.
- **Stories on the Wall** — the elder is fading. Gather the pigments and let
  the painter capture her memories before the moon turns.
- **When the Wolves Sang** — a pack has settled too close. Drive them off —
  or let the shaman learn why they came. (First dog?)
- **The First Seed** — grain grows where last year's camp stood. Discover
  sowing; end the game where farming begins.

## Local co-op: technical design
- **Model**: one shared clan; any player commands any unit (shared control).
- **Input devices**:
  - Player 1: mouse + keyboard (native in Quil/Processing).
  - Player 2: gamepad (XInput controller), polled via JInput or lwjgl's GLFW
    joystick API. Windows exposes gamepads separately from the mouse, so there
    is no device-merging problem (unlike two mice, which need Raw Input/JNA).
  - The gamepad adapter is a polling loop + its own cursor state, emitting the
    same commands the mouse does.
- **Architecture**: the core is player-agnostic. Co-op = *N command producers*
  feeding one command queue; commands are handled one at a time, so
  determinism and event replay are untouched.
- **Player tagging**: commands may carry `:player`, but only the UI layer uses
  it (per-player selection, cursor color). The domain ignores it.
- **Screen**: one shared view, no split screen — shared goals, shared sight.
  Each player has a colored cursor and colored selection ring.

## Engine implications
- The cave is the starting building / spawn point and the recovery place.
- Soft tone = no `:unit-died` event; instead `:unit-scared`, `:unit-exhausted`,
  `:unit-recovered`.
- Individuals: units get age/sex attributes; `:tick` drives eating, aging,
  and baby-chance rolls (seeded RNG recorded in events for replay).
- Seasons: derived from tick count in state, or advanced by events.
- Unit types, paintings (tech) and scenarios are **data-driven** (tagged maps),
  so new content slots in without changing engine code.
- Scenarios: a pure function over the event log / state that answers
  "is the goal reached?" — the sandbox engine never changes.

## Design-driven roadmap (adapt as we go)

### Milestone: the first charming slice (MVP)
Items 2–4. "Viable" = the co-designer leans in and asks "ooh, what's that one
doing?" — ticks passing, foragers gathering, a window showing it. Nothing more.
Playtest with her; let her reactions reorder everything after.

1. ✅ Core loop: `decide` / `evolve` / `handle`, spawn, move, forage.
2. `:tick` — upkeep, foragers gather, hunger. *(MVP)*
3. Map terrain: river, grove, outcrop; resource nodes that deplete.
   (Hand-placed test map — world *generation* comes later.) *(MVP)*
4. **First graphics**: a read-only Quil window that draws the state — terrain,
   units, resources. No input; a thin adapter so the core stays pure.
   (Early on purpose: charm the co-designer.) *(MVP)*
5. Seasons: tick → day → season; seasonal yield modifiers (visible in the window!).
6. World generation: seeded, realistic geography (height → rivers → biomes);
   replaces the hand-placed map. Includes a scrollable camera (UI-only state
   in the render adapter; arrow keys via `:key-pressed`).
7. First art pass: pixel-art tiles. Canonical tiles hand-drawn by the players
   (the engine just loads PNGs); AI-generated variants of the canonicals for
   variety, maybe. Render adapter: `q/load-image`/`q/image` instead of flat
   rects; small native resolution, integer-scale up via off-screen buffer.
8. Mouse input: click a unit, click a tile → commands. Still one player.
   (Pulled forward: trails need movement to mean anything.)
9. Trails: movement wears tiles into tracks/trails/paths; unused trails decay
   each tick; trails speed movement. (Deliberate building comes with the
   craftsman; snow-hiding with seasons polish.)
10. Soft threats: predators that scare; recovery at the cave.
11. Individuals: names, age, sex; eating; babies (seeded randomness in events).
12. Personality & bonds: traits as a fold over each person's experienced
    events; shared experiences build bonds.
13. The painted wall: knowledge and the tech tree as data.
14. Craftsman economy: tools that improve yields; deliberate trail-clearing;
    adornments (beads, feathers, face paint) for decorating people.
15. Cave home-building: improvements (hearth, storage, furs, figurines);
    coziness and beauty as state that affects recovery, winter and babies.
16. Scenario layer: goal functions over the sandbox.
17. Event store, save/load/replay (EDN).
18. Rival clan AI emitting commands (can follow your trails).
19. **Co-op input**: the second player joins with a gamepad (mouse+keyboard
    for player 1, XInput controller for player 2; see co-op technical design).

## Someday / maybe (ideas parked to protect scope)
- What does "losing" mean in a soft-tone sandbox? (A hard winter endured?
  A scenario retried?) Decide after playtesting.
- Cave interior view for decorating (noted under home-building).
- First dog (from "When the Wolves Sang").
- Atmosphere layer: weather drawn over tiles — rain, snow, drifting clouds,
  fireflies, northern lights (art direction's "small touches").
- Passability as a terrain×traveler relation: terrain *requires* capabilities
  (`:river {:requires #{:swimming :boat}}`), people *have* them via the tech
  tree (swimming, boats, climbing tools, snowshoes). Rules stay data; new tech
  adds no code. Introduce with world-gen terrain variety or the tech tree.
