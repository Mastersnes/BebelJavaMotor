package com.bebel.api.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Pools;
import com.bebel.api.actions.complex.WalkAction;
import com.bebel.api.actions.organisation.ParallelAction;
import com.bebel.api.actions.organisation.RepeatAction;
import com.bebel.api.actions.organisation.SequenceAction;
import com.bebel.api.actions.temporal.*;
import com.bebel.api.elements.basique.predicats.AbstractElement;
import com.bebel.api.elements.basique.predicats.DrawableElement;
import com.bebel.api.elements.basique.predicats.MovableElement;
import com.bebel.api.elements.basique.predicats.TransformableElement;
import com.bebel.api.elements.complex.Personnage;
import react.RList;

/**
 * Manager d'actions automatique
 */
public class ActionManager {
    protected static RList<RepeatAction> actions = RList.create();
    static {
        actions.connect(new RList.Listener<RepeatAction>() {
            @Override
            public void onRemove(RepeatAction elem) {
                elem.free();
            }
        });
    }

    /**
     * Demarrage d'une nouvelle sequence
     */
    public static RepeatAction newSequence(final AutomatedAction... actions) {return newSequence(sequence(actions));}
    public static RepeatAction newSequence(final SequenceAction sequence) {
        final RepeatAction repeat = repeat(sequence, 1);
        ActionManager.actions.add(repeat);
        return repeat;
    }
    public static RepeatAction newParallel(final AutomatedAction... actions) {return newParallel(parallel(actions));}
    public static RepeatAction newParallel(final ParallelAction parallel) {
        final RepeatAction repeat = repeat(parallel, 1);
        ActionManager.actions.add(repeat);
        return repeat;
    }

    /**
     * Actions d'odonnancement
     */
    public static SequenceAction sequence(final AutomatedAction... actions) {
        final SequenceAction sequence = action(SequenceAction.class, null);
        sequence.add(actions);
        return sequence;
    }
    public static ParallelAction parallel(final AutomatedAction... actions) {
        final ParallelAction parallel = action(ParallelAction.class, null);
        parallel.add(actions);
        return parallel;
    }
    public static RepeatAction repeat(final AutomatedAction action, final int nb) {
        final RepeatAction repeat = action(RepeatAction.class);
        repeat.init(action, nb);
        return repeat;
    }

    /**
     * Actions classiques
     */
    public static MoveAction move(final MovableElement target) {return action(MoveAction.class, target);}
    public static WalkAction walk(final Personnage target) {return action(WalkAction.class, target);}
    public static ScaleAction scale(final TransformableElement target) {return action(ScaleAction.class, target);}
    public static RotateAction rotate(final TransformableElement target) {return action(RotateAction.class, target);}
    public static ColorAction color(final DrawableElement target) {return action(ColorAction.class, target);}
    public static ColorAction alpha(final DrawableElement target) {return color(target);}
    public static ColorAction fadeOut(final DrawableElement target, final float duration, final Interpolation interpolation) {
        final ColorAction action = action(ColorAction.class, target);
        action.to(0, duration, interpolation);
        return action;
    }
    public static ColorAction fadeIn(final DrawableElement target, final float duration, final Interpolation interpolation) {
        final ColorAction action = action(ColorAction.class, target);
        action.to(1, duration, interpolation);
        return action;
    }
    public static WaitAction delay(final float duration) {
        final WaitAction action = action(WaitAction.class);
        action.init(duration, null);
        return action;
    }
    public static WaitAction wait(final float duration) {return delay(duration);}

    public static RunAction run(final Runnable runnable) {
        final RunAction action = action(RunAction.class, null);
        action.run(runnable);
        return action;
    }

    /**
     * Renvoi une instance de l'action recherché
     * @param actionClass
     * @return
     */
    protected static <ACTION extends AutomatedAction> ACTION action(final Class<ACTION> actionClass) {return action(actionClass, null);}
    protected static <ACTION extends AutomatedAction> ACTION action(final Class<ACTION> actionClass, final AbstractElement target) {
        final ACTION action = Pools.obtain(actionClass);
        action.target(target);
        return action;
    }

    public static void update(final float delta) {
        actions.removeIf(a -> a.update(delta));
    }
}
