package ZorkGame.enums;

public enum AreaStorytelling {

    SUNFIELD(
            "Once a meadow where folk gathered at midsummer, now the lavender grows wild and unchecked. The purple blooms stretch endlessly beneath a sky that never seems quite the right colour. The air hangs thick and sweet, making it hard to tell how long you've been standing there."
    ),

    HAZELWOOD(
    "The hazel trees cluster so tightly their branches knit together overhead, blocking out what little light dares enter. Nuts litter the ground with an unnatural shine, as if they've swallowed moonlight whole. The bark on every trunk bears scratch marks too deliberate to be animal, too numerous to count."
    ),

    ABANDONEDPUB(
    "The last standing structure before the woods claim everything, its sign creaks on rusted chains though there's no wind. Inside, the beer's gone sour and the hearth holds only cold ash, yet someone keeps arranging the chairs in circles. The floorboards remember too many footsteps, and sometimes you'll hear them all at once."
    ),

    HEDGEHOGALP(
    "A hillside of dark stone that locals claim moved three feet south over a single winter. The rocks feel wrong to touch—too smooth, too warm, too aware. Hedgehogs used to nest here in great numbers until they all vanished on the same October night, leaving only their spines arranged in patterns."
    ),

    ASHGROVE(
    "Where the fire took the village forty years gone, ash still coats everything in a fine grey powder. Trees have grown back wrong—twisted, hollow, their roots breaking through the ash like grasping fingers. On still nights you can smell smoke, though nothing's burned here in decades."
    ),

    FOXWOOD(
    "Red berries stain the undergrowth like drops of blood frozen mid-fall. The foxes here are too clever, watching travellers with eyes that understand far more than they should. The trees lean towards you as you pass, and the berries are always ripe, no matter the season."
    ),

    STILLGRASS(
    "Nothing moves here. Not wind, not water, not even the grass that grows tall and sweet-smelling in unnatural silence. The grass grows so thick it could hide anything—and the locals swear it does. Those who camp here wake to find themselves in different spots, the grass around them freshly bent."
    ),

    GREASYBOG(
    "The water here looks like oil, catching light in ways that hurt to watch. Thorns grow from trees that shouldn't bear them, sharp enough to draw blood before you feel the prick. The mud sucks at your boots with purpose, and the will-o'-wisps that dance at dusk look almost like drowning hands."
    ),

    WILDWILLS(
    "Salt deposits burst from the earth in crystalline blooms, beautiful and wrong. They say a man called Will discovered them, though which Will depends on who's telling—there've been seven by that name who've vanished here. The salt tastes of tears and draws water from everything it touches."
    ),

    TALLWOODS(
    "The trees here grew too tall, as if racing to escape something beneath the earth. Their canopy blocks out the sky so completely that day and night mean nothing. Woodsmen marked their trails with carved signs, but the signs rearrange themselves when nobody's looking."
    ),

    MAPLETHICK(
    "Maple trees so densely packed you'd think they were planted by someone with something to hide. The sap runs year-round, sticky and dark, coating the forest floor in a layer that traps small creatures. When autumn comes the leaves don't fall—they simply vanish overnight."
    ),

    CHILCROFTWOOD(
    "Named for the Chilcroft family who once maintained a manor at its edge, now nothing but foundation stones. Ferns carpet the ground so thickly you can't see what you're walking on, and the locals say that's for the best. The Chilcrofts practiced old medicine here, the kind that required more than herbs."
    );


    private final String description;

    AreaStorytelling(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static AreaStorytelling fromString(String text) {
        if (text == null) {
            return null;
        }

        for (AreaStorytelling type : AreaStorytelling.values()) {
            if (type.description.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }

}
