package fr.pederobien.minecraft.rules;

import fr.pederobien.minecraft.dictionary.impl.PlayerGroup;
import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.dictionary.interfaces.IPlayerGroup;

public enum ERuleCode implements IMinecraftCode {
	// Common codes -------------------------------------------------------------

	// Code for the "true" value
	GAME_RULE__TRUE,

	// Code for the "false" value
	GAME_RULE__FALSE,

	// Code for the boolean bad format
	GAME_RULE__BOOLEAN_BAD_FORMAT,

	// Code for the integer bas format
	GAME_RULE__INTEGER_BAD_FORMAT,

	// Code for the "rules" command ---------------------------------------------
	RULES__EXPLANATION,

	// Code for the "rules reset" command ---------------------------------------
	GAME_RULE__RESET__EXPLANATION,

	// Code when the value is reset
	GAME_RULE__RESET__VALUE_RESET,

	// Code for the "get" command -----------------------------------------------
	GAME_RULE__GET__EXPLANATION,

	// Code to display the current game rule value
	GAME_RULE__GET__DISPLAY,

	// Code for the "rules pvp" command -----------------------------------------
	GAME_RULE__PVP__EXPLANATION,

	// Code when the PVP rule value is missing
	GAME_RULE__PVP_SET__VALUE_IS_MISSING,

	// Code when the PVP rule value is updated
	GAME_RULE__PVP_SET__VALUE_UPDATED,

	// Code when the PVP will be activated
	GAME_RULE__PVP__COUNT_DOWN(PlayerGroup.ALL),

	// Code for common command enchant game rule --------------------------------
	GAME_RULE__ENCHANT__ENCHANT_REMOVED,

	// Code for the enchant level completion
	GAME_RULE__ENCHANT__LEVEL_COMPLETION,

	// Code when the level is missing
	GAME_RULE__ENCHANT__LEVEL_IS_MISSING,

	// Code when the level is updated
	GAME_RULE__ENCHANT__LEVEL_UPDATED,

	// Code for the "rules maxProtectionOnDiamonds" command ---------------------
	GAME_RULE__MAX_PROTECTION_ON_DIAMONDS__EXPLANATION,

	// Code when the level is updated
	GAME_RULE__MAX_PROTECTION_ON_DIAMONDS__LEVEL_UPDATED,

	// Code for the "rules revivePlayerNearTeammate" ----------------------------
	GAME_RULE__REVIVE_PLAYER_NEAR_TEAM_MATES__EXPLANATION,

	// Code when the revivePlayerNearTeammate rule value is missing
	GAME_RULE__REVIVE_PLAYER_NEAR_TEAM_MATES_SET__VALUE_IS_MISSING,

	// Code when the revivePlayerNearTeammate rule value is updated
	GAME_RULE__REVIVE_PLAYER_NEAR_TEAM_MATES_SET__VALUE_UPDATED,

	// Code for the "rule announceAdvancements" command -------------------------
	GAME_RULE__ANNOUNCE_ADVANCEMENTS__EXPLANATION,

	// Code when the announceAdvancements rule value is missing
	GAME_RULE__ANNOUNCE_ADVANCEMENTS_SET__VALUE_IS_MISSING,

	// Code when the announceAdvancements rule value is updated
	GAME_RULE__ANNOUNCE_ADVANCEMENTS_SET__VALUE_IS_UPDATED,

	// Code for the "rule naturalRegeneration" command --------------------------
	GAME_RULE__NATURAL_REGENERATION__EXPLANATION,

	// Code when the naturalRegeneration rule value is missing
	GAME_RULE__NATURAL_REGENERATION_SET__VALUE_IS_MISSING,

	// Code when the naturalRegeneration rule value is updated
	GAME_RULE__NATURAL_REGENERATION_SET__VALUE_IS_UPDATED,

	// Code for the "rules mobNotAllowedToSpawn" command ------------------------
	GAME_RULE__MOBS_NOT_ALLOWED_TO_SPAWN__EXPLANATION,

	// Code when all mobs are allowed to spawn
	GAME_RULE__MOBS_NOT_ALLOWED_TO_SPAWN_SET__ALL_MOBS_ALLOWED,

	// Code when the mob does not exists
	GAME_RULE__MOBS_NOT_ALLOWED_TO_SPAWN_SET__MOB_NOT_FOUND,

	// Code when one mob is not allowed to spawn
	GAME_RULE__MOBS_NOT_ALLOWED_TO_SPAWN_SET__ONE_MOB_NOT_ALLOWED,

	// Code when several mobs are not allowed to spawn
	GAME_RULE__MOBS_NOT_ALLOWED_TO_SPAWN_SET__SEVERAL_MOBS_NOT_ALLOWED,

	// Code for the "rules displayTeamMatesLocation" command --------------------
	GAME_RULE__DISPLAY_TEAM_MATES_LOCATION__EXPLANATION,

	// Code when the displayTeamMatesLocation rule value is missing
	GAME_RULE__DISPLAY_TEAM_MATES_LOCATION_SET__VALUE_IS_MISSING,

	// Code when the displayTeamMatesLocation rule value is updated
	GAME_RULE__DISPLAY_TEAM_MATES_LOCATION_SET__VALUE_IS_UPDATED,

	// Code for the "rules enable" command --------------------------------------
	GAME_RULE__ENABLE__EXPLANATION,

	// Code when the enable value is missing
	GAME_RULE__ENABLE__VALUE_IS_MISSING,

	// Code when the rule is enabled
	GAME_RULE__ENABLE__RULE_ENABLED,

	// Code when the rule is disabled
	GAME_RULE__ENABLE__RULE_DISABLED,

	;

	private IPlayerGroup group;

	private ERuleCode() {
		this(PlayerGroup.OPERATORS);
	}

	private ERuleCode(IPlayerGroup group) {
		this.group = group;
	}

	@Override
	public String value() {
		return name();
	}

	@Override
	public IPlayerGroup getGroup() {
		return group;
	}

	@Override
	public void setGroup(IPlayerGroup group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return String.format("value=%s,group=%s", value(), getGroup());
	}
}
