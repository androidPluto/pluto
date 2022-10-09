package com.pluto.tools

class ToolManager {

    private var plugins: LinkedHashSet<PlutoTool> = linkedSetOf()
    internal val installedPlugins: List<PlutoTool>
        get() {
            val list = arrayListOf<PlutoTool>()
            list.addAll(plugins)
            return list
        }

    fun initialise() {
//        plugins.forEach {
//            it.initialise(application)
//        }
    }
}
