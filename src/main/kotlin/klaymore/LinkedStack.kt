package klaymore

class LinkedStack<T> {
    class Item<T>(val value: T) {
        var next: Item<T>? = null
    }

    var size = 0
        private set

    private var head: Item<T>? = null

    fun addFirst(value: T) {
        val item = Item(value)
        if (head == null) {
            head = item
        } else {
            item.next = head
            head = item
        }
        size++
    }

    fun removeAll(value: T): Boolean {
        val oldSize = size
        while (head != null && head!!.value == value) {
            head = head!!.next
            size--
        }
        if (head != null) removeAllFromSecond(value)
        return oldSize != size
    }

    private fun removeAllFromSecond(value: T) {
        var item = head?.next
        var prev: Item<T>? = head
        while (item != null) {
            if (item.value == value) {
                prev!!.next = item.next
                size--
            } else {
                prev = item
            }
            item = item.next
        }
    }

    fun moveFirst(value: T): Boolean {
        val oldSize = size
        if (head == null || head!!.value != value) {
            addFirst(value)
        }
        removeAllFromSecond(value)
        return oldSize != size
    }

    fun toList(): List<T> {
        val list = ArrayList<T>(size)
        var item = head
        while (item != null) {
            list.add(item.value)
            item = item.next
        }
        return list
    }
}
