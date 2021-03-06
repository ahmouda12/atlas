package org.openstreetmap.atlas.geography.atlas.raw.slicing;

import java.util.Iterator;
import java.util.Map;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.openstreetmap.atlas.geography.Location;
import org.openstreetmap.atlas.geography.atlas.Atlas;
import org.openstreetmap.atlas.geography.atlas.items.Relation;
import org.openstreetmap.atlas.geography.atlas.items.complex.Finder;
import org.openstreetmap.atlas.geography.atlas.items.complex.water.ComplexWaterEntity;
import org.openstreetmap.atlas.geography.atlas.items.complex.water.finder.ComplexWaterEntityFinder;
import org.openstreetmap.atlas.geography.atlas.pbf.AtlasLoadingOption;
import org.openstreetmap.atlas.geography.boundary.CountryBoundaryMap;
import org.openstreetmap.atlas.streaming.compression.Decompressor;
import org.openstreetmap.atlas.streaming.resource.InputStreamResource;
import org.openstreetmap.atlas.tags.ISOCountryTag;
import org.openstreetmap.atlas.utilities.collections.Iterables;

/**
 * {@link RawAtlasSlicer} unit tests for slicing {@link Relation}s.
 *
 * @author mgostintsev
 * @author samg
 */
public class RelationSlicingTest
{
    private static AtlasLoadingOption loadingOption;

    private static ComplexWaterEntityFinder complexWaterEntityFinder = new ComplexWaterEntityFinder();

    static
    {
        loadingOption = AtlasLoadingOption.createOptionWithAllEnabled(CountryBoundaryMap
                .fromPlainText(new InputStreamResource(() -> LineAndPointSlicingTest.class
                        .getResourceAsStream("CIV_GIN_LBR_osm_boundaries_with_grid_index.txt.gz"))
                                .withDecompressor(Decompressor.GZIP)));
        loadingOption.setAdditionalCountryCodes("CIV", "GIN", "LBR");
    }

    @Rule
    public RelationSlicingTestRule setup = new RelationSlicingTestRule();

    @Test
    public void testMultiPolygonRelationSpanningTwoCountries()
    {
        // This relation is made up of three closed lines, each serving as an outer to a
        // multipolygon relation. Two of the outers span the border of two countries, while one is
        // entirely within a country.
        final Atlas rawAtlas = this.setup.getSimpleMultiPolygonAtlas();
        Assert.assertEquals(3, rawAtlas.numberOfLines());
        Assert.assertEquals(12, rawAtlas.numberOfPoints());
        Assert.assertEquals(1, rawAtlas.numberOfRelations());

        final Atlas slicedAtlas = new RawAtlasSlicer(loadingOption, rawAtlas).slice();

        Assert.assertEquals(0, slicedAtlas.numberOfPoints());
        Assert.assertEquals(9, slicedAtlas.numberOfLines());
        Assert.assertEquals(2, slicedAtlas.numberOfRelations());
    }

    @Test
    public void testMultiPolygonWithClosedLinesSpanningTwoCountries()
    {
        // This relation is made up of closed lines, tied together by a relation, to create a
        // MultiPolygon with the outer spanning two countries and the inner fully inside one
        // country.
        final Atlas rawAtlas = this.setup.getComplexMultiPolygonWithHoleUsingClosedLinesAtlas();

        Assert.assertEquals(2, rawAtlas.numberOfLines());
        Assert.assertEquals(9, rawAtlas.numberOfPoints());
        Assert.assertEquals(1, rawAtlas.numberOfRelations());

        final Atlas slicedAtlas = new RawAtlasSlicer(loadingOption, rawAtlas).slice();

        Assert.assertEquals(5, slicedAtlas.numberOfLines());
        Assert.assertEquals(0, slicedAtlas.numberOfPoints());
        Assert.assertEquals(2, slicedAtlas.numberOfRelations());

        // Just for fun (and to validate the sliced multi-polygon validity) - create Complex
        // Entities and make sure they are valid.
        final Iterable<ComplexWaterEntity> waterEntities = this.complexWaterEntityFinder
                .find(slicedAtlas, Finder::ignore);
        Assert.assertEquals(2, Iterables.size(waterEntities));
    }

    @Test
    public void testMultiPolygonWithOpenLinesSpanningTwoCountries()
    {
        // This relation is made up of open lines, tied together by a relation to create a
        // MultiPolygon with the outer spanning two countries and the inner fully inside one
        // country.
        final Atlas rawAtlas = this.setup.getComplexMultiPolygonWithHoleUsingOpenLinesAtlas();

        Assert.assertEquals(4, rawAtlas.numberOfLines());
        Assert.assertEquals(9, rawAtlas.numberOfPoints());
        Assert.assertEquals(1, rawAtlas.numberOfRelations());

        final Atlas slicedAtlas = new RawAtlasSlicer(loadingOption, rawAtlas).slice();
        Assert.assertEquals(8, slicedAtlas.numberOfLines());
        Assert.assertEquals(11, slicedAtlas.numberOfPoints());
        Assert.assertEquals(2, slicedAtlas.numberOfRelations());

        // Just for fun (and to validate the sliced multi-polygon validity) - create Complex
        // Entities and make sure they are valid.
        final Iterable<ComplexWaterEntity> waterEntities = this.complexWaterEntityFinder
                .find(slicedAtlas, Finder::ignore);
        Assert.assertEquals(2, Iterables.size(waterEntities));
    }

    @Test
    public void testSimpleMultiPolygonWithHoleSpanningTwoCountries()
    {
        // This relation is made up of two closed lines, forming a multi-polygon with one inner and
        // one outer, both spanning the boundary of two countries.
        final Atlas rawAtlas = this.setup.getSimpleMultiPolygonWithHoleAtlas();

        Assert.assertEquals(2, rawAtlas.numberOfLines());
        Assert.assertEquals(8, rawAtlas.numberOfPoints());
        Assert.assertEquals(1, rawAtlas.numberOfRelations());

        final Atlas slicedAtlas = new RawAtlasSlicer(loadingOption, rawAtlas).slice();

        Assert.assertEquals(0, slicedAtlas.numberOfPoints());
        Assert.assertEquals(8, slicedAtlas.numberOfLines());
        Assert.assertEquals(2, slicedAtlas.numberOfRelations());
    }

    @Test
    public void testSingleOuterMadeOfOpenLinesSpanningTwoCountries()
    {
        // This relation is made up of two open lines, both crossing the country boundary and
        // forming a multi-polygon with one outer.
        final Atlas rawAtlas = this.setup.getSingleOuterMadeOfOpenLinesSpanningTwoCountriesAtlas();
        Assert.assertEquals(2, rawAtlas.numberOfLines());
        Assert.assertEquals(9, rawAtlas.numberOfPoints());
        Assert.assertEquals(1, rawAtlas.numberOfRelations());

        final Atlas slicedAtlas = new RawAtlasSlicer(loadingOption, rawAtlas).slice();

        Assert.assertEquals(9, slicedAtlas.numberOfPoints());
        Assert.assertEquals(6, slicedAtlas.numberOfLines());
        Assert.assertEquals(2, slicedAtlas.numberOfRelations());

    }

    @Test
    public void testSingleOuterMadeOfOpenLinesSpanningTwoCountriesWithDuplicatePoints()
    {
        // This relation is made up of two open lines, both crossing the country boundary and
        // forming a multi-polygon with one outer.
        final Atlas rawAtlas = this.setup
                .getSingleOuterMadeOfOpenLinesSpanningTwoCountriesAtlasWithDuplicatePoints();
        Assert.assertEquals(2, rawAtlas.numberOfLines());
        Assert.assertEquals(11, rawAtlas.numberOfPoints());
        Assert.assertEquals(1, rawAtlas.numberOfRelations());

        final Atlas slicedAtlas = new RawAtlasSlicer(loadingOption, rawAtlas).slice();

        Assert.assertEquals(9, slicedAtlas.numberOfPoints());
        Assert.assertEquals(6, slicedAtlas.numberOfLines());
        Assert.assertEquals(2, slicedAtlas.numberOfRelations());

        slicedAtlas.lines().forEach(line ->
        {
            final Iterator<Location> lineLocations = line.iterator();
            Location previous = lineLocations.next();
            while (lineLocations.hasNext())
            {
                final Location current = lineLocations.next();
                Assert.assertFalse(current.equals(previous));
                previous = current;
            }
        });
    }

    @Test
    public void testSlicingOnRelationWithOnlyRelationsAsMembers()
    {
        final Atlas rawAtlas = this.setup.getRelationWithOnlyRelationsAsMembers();

        Assert.assertEquals(2, rawAtlas.numberOfPoints());
        Assert.assertEquals(3, rawAtlas.numberOfRelations());

        final Atlas slicedAtlas = new RawAtlasSlicer(loadingOption, rawAtlas).slice();
        for (final Relation relation : slicedAtlas.relations())
        {
            final Map<String, String> tags = relation.getTags();

            Assert.assertTrue(tags.containsKey(ISOCountryTag.KEY));
            Assert.assertNotEquals(ISOCountryTag.COUNTRY_MISSING, tags.get(ISOCountryTag.KEY));
        }
    }
}
