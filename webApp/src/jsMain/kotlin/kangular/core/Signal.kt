package kangular.core

/* Signal reads are not implemented. They don't work. Only
 * reads managed by Angular trigger render state reactions.
 */
interface WritableSignal<T> {
  fun set(value: T)
}

class AngularWritable<T>(val ngSignal: dynamic) : WritableSignal<T> {
  override fun set(value: T) = ngSignal.set(value)
}
