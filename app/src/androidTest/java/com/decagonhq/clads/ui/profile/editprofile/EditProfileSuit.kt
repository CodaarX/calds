package com.decagonhq.clads.ui.profile.editprofile

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    AccountFragmentTest::class,
    EditProfileFragmentTest::class,
    MapFragmentTest::class,
    PaymentMethodFragmentTest::class,
    SecurityFragmentTest::class,
    SpecialtyFragmentTest::class,
    SpecialtyModelFragmentTest::class
)
class EditProfileSuit
