package gg.warcraft.legends.spigot.OLD.ai;

public class AIModule {
    /*
	private val entities = HashSet<MonolithEntity>()
	private val listeners = HashMap<MonolithEntity, ArrayList<MonolithEntityListener>>()

	init {
		for (t in MonolithEntity.Type.values())
			Monolith.log(Level.INFO, "$this: Initialising " + t.name + ".")
		Monolith.registerForEvents(this)
	}

	companion object {
		private val instance = AIModule()

		fun spawn(entity: MonolithEntity, location: Location) {
			if (!location.chunk.isLoaded)
				location.chunk.load()

			val nmsEntity = entity as Entity
			nmsEntity.setLocation(location.x, location.y, location.z, 0f, 0f)
			if ((location.world as CraftWorld).handle.addEntity(nmsEntity, CreatureSpawnEvent.SpawnReason.CUSTOM))
				instance.entities.add(entity)
			else
				Monolith.log(Level.SEVERE, "$this: Could not spawn $entity.")
		}

		fun registerListener(entity: MonolithEntity, listener: MonolithEntityListener) {
			if (instance.listeners.containsKey(entity))
				instance.listeners[entity]!!.add(listener)
			else {
				val list = ArrayList<MonolithEntityListener>()
				list.add(listener)
				instance.listeners.put(entity, list)
			}
		}
	}

	private fun getMonolithEntity(event: EntityEvent): MonolithEntity? {
		val entity = (event.entity as CraftEntity).handle
		if (entity is MonolithEntity)
			return entity
		else
			return null
	}

	override fun toString(): String {
		return "AIModule"
	}

	@EventHandler
	fun onChunkUnloadEvent(event: ChunkUnloadEvent) {
		for (entity in event.chunk.entities)
			if (entity is MonolithEntity) {
				event.isCancelled = true
				break
			}
	}

	@EventHandler
	fun onEntityCombustEvent(event: EntityCombustEvent) {
		if (getMonolithEntity(event) != null)
			event.isCancelled = true
	}

	@EventHandler
	fun onEntityDeathEvent(event: EntityDeathEvent) {
		if (event.entity !is Player) {
			val entity = getMonolithEntity(event)
			if (entity != null && listeners.containsKey(entity)) {
				for (listener in listeners[entity]!!.iterator())
					listener.onMonolithEntityDeathEvent(event, entity)
				listeners.remove(entity)
			}
		}
	}
     */
}
