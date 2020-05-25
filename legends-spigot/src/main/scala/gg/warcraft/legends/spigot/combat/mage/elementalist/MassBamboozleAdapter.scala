package gg.warcraft.legends.spigot.combat.mage.elementalist

import java.util.UUID

import gg.warcraft.legends.api.combat.mage.wizard.MassBamboozle
import me.libraryaddict.disguise.disguisetypes.{DisguiseType, MobDisguise}
import me.libraryaddict.disguise.DisguiseAPI
import org.bukkit.Server

import scala.util.Random

class MassBamboozleAdapter(implicit server: Server) extends MassBamboozle.Adapter {
  private final val disguises: List[MobDisguise] =
    (DisguiseType.COW :: DisguiseType.PIG :: DisguiseType.SHEEP :: DisguiseType.TURTLE :: Nil)
      .map { new MobDisguise(_) }

  private def randomDisguise: MobDisguise = {
    val count = disguises.size
    val index = Random.nextInt(count)
    disguises(index)
  }

  override def disguise(entityId: UUID): Unit = {
    val disguise = randomDisguise
    val entity = server.getEntity(entityId)
    DisguiseAPI.disguiseToAll(entity, disguise)
  }
  override def reset(entityId: UUID): Unit = {
    val entity = server.getEntity(entityId)
    DisguiseAPI.undisguiseToAll(entity)
  }
}
