package com.decagonhq.clads.ui.resource


import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
    IndividualVideoScreenFragmentTest::class,
    ResourceArticlesFragment::class,
    ResourceGeneralFragment::class,
    ResourceVideosFragment::class
)
class ResourceSuit