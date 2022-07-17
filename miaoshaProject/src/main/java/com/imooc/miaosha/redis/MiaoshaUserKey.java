package com.imooc.miaosha.redis;

/**
 * 用户在缓存中存贮的 键前缀字符串
 */
public class MiaoshaUserKey extends BaseKeyPrefix{

	public static final int TOKEN_EXPIRE = 3600 * 24 * 2;	// 统一 设置用户 token 有效时间
	private String prefix ;
	private MiaoshaUserKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
		this.prefix = prefix;
	}

	// 用户 token（用户令牌）
	public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE, "tk");
	// 用户 id （用户手机号）
	public static MiaoshaUserKey getById = new MiaoshaUserKey(0, "id");
	
	public MiaoshaUserKey withExpire(int seconds) {
		return new MiaoshaUserKey(seconds, prefix);
	}
}
