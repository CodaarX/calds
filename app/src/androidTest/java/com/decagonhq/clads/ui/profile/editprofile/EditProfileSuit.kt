package com.decagonhq.clads.ui.profile.editprofile

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    AccountFragmentTest::class,
    EditProfileFragmentTest::class,
    PaymentMethodFragmentTest::class,
    HomeFragmentTest::class,
    SecurityFragmentTest::class,
    SpecialtyFragmentTest::class,
)
class EditProfileSuit
