/*
 * MIT License
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package gg.warcraft.legends.api.core.data

import gg.warcraft.monolith.api.core.event.EventService

import java.util.logging.Logger

class LegendsServerDataService(implicit
    logger: Logger,
    repository: LegendsServerDataRepository,
    eventService: EventService
) {
  private final val ERR_MISSING_SERVER_DATA =
    "Legends server data is missing from the database! " +
      "Current active game will be reset. " +
      "If this is the first time Legends is running this is normal."

  private var _data: LegendsServerData = _
  def data: LegendsServerData = _data

  repository.load match {
    case Some(data) => _data = data
    case None =>
      logger.severe(ERR_MISSING_SERVER_DATA)
      _data = LegendsServerData(-1, -1)
  }

  def setActiveWar(gameId: Long): Unit = {
    val updated = data.copy(activeWar = gameId)
    _data = updated
    repository.save(updated)
  }

  def setActiveSkirmish(gameId: Long): Unit = {
    val updated = data.copy(activeSkirmish = gameId)
    _data = updated
    repository.save(updated)
  }
}
