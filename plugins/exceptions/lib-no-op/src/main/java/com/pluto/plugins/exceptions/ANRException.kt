package com.pluto.plugins.exceptions

import androidx.annotation.Keep

@Keep
@SuppressWarnings("UnusedPrivateMember", "EmptyFunctionBlock")
class ANRException(thread: Thread) : Exception("ANR detected")
