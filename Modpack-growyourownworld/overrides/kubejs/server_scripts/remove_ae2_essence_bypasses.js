// AE2PLAN #18 gate integrity — Mystical Agriculture ships essence recipes that craft AE2's meteorite-only items from
// Certus Quartz Essence (data/mysticalagriculture/recipe/essence/appliedenergistics2/*). Two of those bypass the
// whole endgame gate and are removed here:
//   - the four inscriber PRESSES (calculation/engineering/logic/silicon) — presses come only from the meteor island's
//     Mysterious Cube, per the design;
//   - SKY STONE — the ME Controller material; the meteor island must be its ONLY source.
// The certus_quartz / certus_quartz_dust / fluix / fluix_dust essence recipes are KEPT — those are renewable mid-game
// resources, not the gate.
ServerEvents.recipes(event => {
  [
    'calculation_press',
    'engineering_press',
    'logic_press',
    'silicon_press',
    'sky_stone'
  ].forEach(name => event.remove({ id: `mysticalagriculture:essence/appliedenergistics2/${name}` }))
})
