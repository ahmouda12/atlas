package org.openstreetmap.atlas.geography.atlas.raw.sectioning;

import org.openstreetmap.atlas.geography.atlas.Atlas;
import org.openstreetmap.atlas.utilities.testing.CoreTestRule;
import org.openstreetmap.atlas.utilities.testing.TestAtlas;

/**
 * {@link WaySectionProcessorTest} test data.
 *
 * @author mgostintsev
 */
public class WaySectionProcessorTestRule extends CoreTestRule
{
    @TestAtlas(loadFromTextResource = "bidirectionalRing.atlas.txt")
    private Atlas bidirectioalRingAtlas;

    @TestAtlas(loadFromTextResource = "loopingWayWithIntersection.atlas.txt")
    private Atlas loopingWayWithIntersection;

    @TestAtlas(loadFromTextResource = "oneWayRing.atlas.txt")
    private Atlas oneWayRing;

    @TestAtlas(loadFromTextResource = "oneWaySimpleLine.atlas.txt")
    private Atlas oneWaySimpleLine;

    @TestAtlas(loadFromTextResource = "reversedOneWayLine.atlas.txt")
    private Atlas reversedOneWayLine;

    @TestAtlas(loadFromTextResource = "lineWithLoopAtEnd.atlas.txt")
    private Atlas lineWithLoopAtEnd;

    @TestAtlas(loadFromTextResource = "lineWithLoopAtStart.atlas.txt")
    private Atlas lineWithLoopAtStart;

    @TestAtlas(loadFromTextResource = "lineWithBarrier.atlas.txt")
    private Atlas lineWithBarrier;

    @TestAtlas(loadFromTextResource = "simpleBiDirectionalLine.atlas.txt")
    private Atlas simpleBiDirectionalLine;

    @TestAtlas(loadFromTextResource = "ringWithSingleIntersection.atlas.txt")
    private Atlas ringWithSingleIntersection;

    @TestAtlas(loadFromTextResource = "roundAbout.atlas.txt")
    private Atlas roundAbout;

    public Atlas getBidirectionalRingAtlas()
    {
        return this.bidirectioalRingAtlas;
    }

    public Atlas getLineWithBarrierAtlas()
    {
        return this.lineWithBarrier;
    }

    public Atlas getLineWithLoopAtEndAtlas()
    {
        return this.lineWithLoopAtEnd;
    }

    public Atlas getLineWithLoopAtStartAtlas()
    {
        return this.lineWithLoopAtStart;
    }

    public Atlas getLoopingWayWithIntersectionAtlas()
    {
        return this.loopingWayWithIntersection;
    }

    public Atlas getOneWayRingAtlas()
    {
        return this.oneWayRing;
    }

    public Atlas getOneWaySimpleLineAtlas()
    {
        return this.oneWaySimpleLine;
    }

    public Atlas getReversedOneWayLineAtlas()
    {
        return this.reversedOneWayLine;
    }

    public Atlas getRingWithSingleIntersectionAtlas()
    {
        return this.ringWithSingleIntersection;
    }

    public Atlas getRoundAboutAtlas()
    {
        return this.roundAbout;
    }

    public Atlas getSimpleBiDirectionalLineAtlas()
    {
        return this.simpleBiDirectionalLine;
    }
}