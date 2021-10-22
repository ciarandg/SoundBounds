package com.ciarandg.soundbounds.client.regions.blocktree

class BlockTree : MutableSet<Vec3iConst> {
    private var rootNode: BlockTreeNode? = null

    override val size: Int
        get() = rootNode?.blockCount() ?: 0

    override fun add(element: Vec3iConst): Boolean =
        when (val root = rootNode) {
            null -> {
                rootNode = BlockTreeNodeBlack(element)
                true
            }
            else -> {
                if (root.canContain(element)) root.add(element)
                else {
                    rootNode = BlockTreeNodeGrey(root, BlockTreeNodeBlack(element))
                    true
                }
            }
        }

    override fun addAll(elements: Collection<Vec3iConst>): Boolean {
        var hasNewElement = false
        elements.forEach {
            val isNew = add(it)
            if (isNew) hasNewElement = true
        }
        return hasNewElement
    }

    override fun clear() {
        rootNode = null
    }

    override fun iterator(): MutableIterator<Vec3iConst> = when (val root = rootNode) {
        null -> BlockTreeNodeWhite.iterator()
        else -> root.iterator()
    }

    override fun remove(element: Vec3iConst): Boolean =
        when (val root = rootNode) {
            null -> false
            else -> if (root.canContain(element)) root.remove(element) else false
        }

    override fun removeAll(elements: Collection<Vec3iConst>) = elements.any { remove(it) }

    override fun retainAll(elements: Collection<Vec3iConst>): Boolean {
        val it = iterator()
        var elementWasRemoved = false
        while (it.hasNext()) {
            if (!elements.contains(it.next())) {
                elementWasRemoved = true
                it.remove()
            }
        }
        return elementWasRemoved
    }

    override fun contains(element: Vec3iConst) = rootNode?.contains(element) ?: false

    override fun containsAll(elements: Collection<Vec3iConst>) = elements.all { contains(it) }

    override fun isEmpty() = rootNode == null
}