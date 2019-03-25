/*package model23.celou;

import java.util.Random;

import javax.swing.text.View;

public class A {

}
public class StrategyActivity {


	//@BindView(R.id.zombie_tv)
	// TextView mZombieTv;
	// @BindView(R.id.speed_tv)
	// TextView mSpeedTv;
	// @BindView(R.id.attack_tv)
	// TextView mAttackTv;

	private Character mCharacter;
	private IAttackBehavior mIAttackBehavior;
	private ISpeedBehavior mISpeedBehavior;
	private int mRandomZombieNum = 0;
	private int mRandomSpeedNum = 0;
	private int mRandomAttackNum = 0;
	private Random mRandom;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_strategy);
		//ButterKnife.bind(this);
		//ƒ¨»œ…Ë÷√
		mIAttackBehavior = new OrdinaryAttack();
		mISpeedBehavior = new NormalSpeed();
		//mCharacter = new RedHeadZombie(mIAttackBehavior,mISpeedBehavior);
		mRandom = new Random();
	}

	//@OnClick({R.id.btn_change_zombie, R.id.btn_change_speed, R.id.btn_change_attack})
	public void onViewClicked(View view) {
		//switch (view.getId()) {
		case R.id.btn_change_zombie:
			mRandomZombieNum = mRandom.nextInt(4);
			switch (mRandomZombieNum){
			case 0:
				mCharacter = new RedHeadZombie(mIAttackBehavior,mISpeedBehavior);
				mZombieTv.setText("∫ÏÕ∑Ω© ¨");
				break;
			case 1:
				mCharacter = new GreenHeadZombie(mIAttackBehavior,mISpeedBehavior);
				mZombieTv.setText("¬ÃÕ∑Ω© ¨");
				break;
			case 2:
				mCharacter = new ShortLegZombie(mIAttackBehavior,mISpeedBehavior);
				mZombieTv.setText("∂ÃÕ»Ω© ¨");
				break;
			case 3:
				mCharacter = new NoAttackZombie(mIAttackBehavior,mISpeedBehavior);
				mZombieTv.setText("Œﬁπ•ª˜¡¶Ω© ¨");
				break;
			}
			mCharacter.attack();
			mCharacter.speed();
			break;
		case R.id.btn_change_speed:
			mRandomSpeedNum = mRandom.nextInt(3);
			switch (mRandomSpeedNum){
			case 0:
				mISpeedBehavior = new NormalSpeed();
				mSpeedTv.setText("My speed is normal");
				break;
			case 1:
				mISpeedBehavior = new SlowSpeed();
				mSpeedTv.setText("My speed is slow");
				break;
			case 2:
				mISpeedBehavior = new FastSpeed();
				mSpeedTv.setText("My speed is fast");
				break;
			}
			mCharacter.setISpeedBehavior(mISpeedBehavior);
			mCharacter.speed();
			break;
		case R.id.btn_change_attack:
			mRandomAttackNum = mRandom.nextInt(3);
			switch (mRandomAttackNum){
			case 0:
				mIAttackBehavior = new OrdinaryAttack();
				mAttackTv.setText("I use ordinary rtdinary attack");
				break;
			case 1:
				mIAttackBehavior = new ReinforceAttack();
				mAttackTv.setText("I use ordinary reinforce attack");
				break;
			case 2:
				mIAttackBehavior = new SuperAttack();
				mAttackTv.setText("I use ordinary super attack");
				break;
			case 3:
				break;
			}
			mCharacter.setIAttackBehavior(mIAttackBehavior);
			mCharacter.attack();
			break;
		}
	}
}*/