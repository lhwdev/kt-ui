package com.lhwdev.ktui


interface Mergeable<This : Any> {
	fun merge(other: This): This
}
