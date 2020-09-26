package fr.pederobien.minecraftrules;

import fr.pederobien.minecraftdictionary.impl.Permission;
import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;

public enum EGameRuleMessageCode implements IMinecraftMessageCode {
	// Code for command reset
	RESET_GAME_RULE__EXPLANATION, RESET_GAME_RULE__VALUE_RESET,

	// Code for command currentValue
	CURRENT_VALUE_GAME_RULE__EXPLANATION, CURRENT_VALUE_GAME_RULE__DISPLAY,

	// Code for command set
	SET_GAME_RULE__EXPLANATION,

	// Code for common game rule
	COMMON_VALUE_IS_MISSING, COMMON_VALUE_DEFINED, COMMON_VALUE_DEFINED_IN_GAME,

	// Code for command rules
	RULES__EXPLANATION,

	// Code for command pvp
	PVP_GAME_RULE__EXPLANATION, PVP__COUNT_DOWN(Permission.ALL),

	// Code for command revivePlayerNearTeammate
	REVIVE_PLAYER_NEAR_TEAMMATE__EXPLANATION,

	// Code for command displayCurrentTeammateLocation
	DISPLAY_CURRENT_TEAMMATES_LOCATION__EXPLANATION,

	// Code for common command enchant game rule
	ENCHANT_GAME_RULE__CANNOT_ENCHANT, ENCHANT_GAME_RULE__ON_TAB_COMPLETE,

	// Code for command maxProtectionOnDiamonds
	MAX_PROTECTION_ON_DIAMONDS__EXPLANATION,

	// Code for command announceAdvancements
	ANNOUNCE_ADVANCEMENTS_GAME_RULE__EXPLANATION,

	// Code for command mobNotAllowedToSpawn
	MOBS_NOT_ALLOWED_TO_SPAWN_GAME_RULE__EXPLANATION, MOBS_NOT_ALLOWED_TO_SPAWN_GAME_RULE__ALL_MOBS_ALLOWED, MOBS_NOT_ALLOWED_TO_SPAWN_GAME_RULE__MOB_UNDEFINED,
	MOBS_NOT_ALLOWED_TO_SPAWN_GAME_RULE__ONE_MOB_NOT_ALLOWED, MOBS_NOT_ALLOWED_TO_SPAWN_GAME_RULE__SEVERAL_MOBS_NOT_ALLOWED, MOBS_NOT_ALLOWED_TO_SPAWN_GAME_RULE__DEFAULT;

	private Permission permission;

	private EGameRuleMessageCode() {
		this(Permission.OPERATORS);
	}

	private EGameRuleMessageCode(Permission permission) {
		this.permission = permission;
	}

	@Override
	public String value() {
		return toString();
	}

	@Override
	public Permission getPermission() {
		return permission;
	}

	@Override
	public void setPermission(Permission permission) {
		this.permission = permission;
	}
}
