package com.decagonhq.clads.ui.client

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    AddAddressFragmentTest::class,
    ClientAccountFragmentTest::class,
    ClientDetailsFragmentTest::class,
    ClientFragmentTest::class,
    DeliveryAddressFragmentTest::class,
    MeasurementsFragmentTest::class
)
class ClientSuit
