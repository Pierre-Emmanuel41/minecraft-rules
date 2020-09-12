package fr.pederobien.minecraftrules;

import fr.pederobien.minecraftdictionary.impl.Permission;
import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;

public enum EGameRuleMessageCode implements IMinecraftMessageCode {
	// Code for command reset
	RESET_GAME_RULE__EXPLANATION, RESET_GAME_RULE__VALUE_RESET,

	// Code for command value
	VALUE_GAME_RULE__EXPLANATION, VALUE_GAME_RULE__DISPLAY,

	// Code for command set
	SET_GAME_RULE__EXPLANATION,

	// Code for common game rule
	COMMON_VALUE_IS_MISSING, COMMON_VALUE_DEFINED, COMMON_VALUE_DEFINED_IN_GAME,

	// Code for command rules
	RULES__EXPLANATION,

	// Code for command pvp
	PVP_GAME_RULE__EXPLANATION,

	// Code for command revivePlayerNearTeammate
	REVIVE_PLAYER_NEAR_TEAMMATE__EXPLANATION,

	// Code for command displayCurrentTeammateLocation
	DISPLAY_CURRENT_TEAMMATES_LOCATION__EXPLANATION;

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
