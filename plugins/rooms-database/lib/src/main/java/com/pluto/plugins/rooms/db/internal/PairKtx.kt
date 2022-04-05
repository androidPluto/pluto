package com.pluto.plugins.rooms.db.internal

internal fun <A, B> Pair<Iterable<A>, Iterable<B>>.forEachIndexed(action: (Int, A, B) -> Unit) {
    val ia = first.iterator().withIndex()
    val ib = second.iterator().withIndex()

    while (ia.hasNext() && ib.hasNext()) {
        val next = ia.next()
        val index = next.index
        val va = next.value
        val vb = ib.next().value

        action(index, va, vb)
    }
}
