package com.bebel.api.elements.complex;

/**
 * Couche representant un personnage
 */
//public class Personnage extends AnimableElement {
//    public Personnage(final String name, final float w, final float h, final float speed, final float acceleration) {
//        super(name, w, h);
//        this.maxSpeed = speed;
//        this.acceleration = acceleration;
//        onMouvementChange(m -> currentSpeed = 0);
//    }
//
//    @Override
//    public boolean update(float delta) {
//        super.update(delta);
//        if ((mouvementX || mouvementY) && (currentSpeed < maxSpeed)) {
//            currentSpeed += acceleration * maxSpeed;
//            currentSpeed = MathUtils.clamp(currentSpeed, 0, maxSpeed);
//        }
//
//        if (canMoveX() && mouvementX) move(directionX.sens() * currentSpeed * delta, 0);
//        if (canMoveY() && mouvementY) {
//            move(0, directionY.sens() * currentSpeed * delta);
//            if (!ignoreZ)
//                grow(directionY.sens() * delta * currentSpeed/maxSpeed * 0.1f);
//        }
//        updateAnims(); return true;
//    }
//
//    public void updateAnims() {
//        String dx = D_NULL, dy = D_NULL;
//        if (canMoveX() && mouvementX) dx = directionX.code();
//        if (canMoveY() && mouvementY) dy = directionY.code();
//
//        if (!mouvementX && !mouvementY) {
//            if (directionX != NULL && (canMoveXY() || !lastDirection.vertical() || directionY == NULL))
//                dx = D_IDLE + directionX.code();
//            if (directionY != NULL && (canMoveXY() || lastDirection.vertical() || directionX == NULL))
//                dy = D_IDLE + directionY.code();
//        }else if (mouvementX && mouvementY && !canMoveXY()) {
//            if (lastDirection.vertical()) dx = D_NULL;
//            else dy = D_NULL;
//        }
//
//        if (dx.equals(D_NULL) && dy.equals(D_NULL)) play(D_IDLE + D_RIGHT);
//        else play(dx + dy);
//    }
//
//    /**
//     * Mouvement
//     */
//    // Derniere direction prise
//    protected Direction lastDirection = NULL;
//    // Direction prise par le personnage
//    protected Direction directionX = NULL, directionY = NULL;
//    protected Signal<Direction> onDirectionChange;
//
//    //Indique de ne pas toucher à la profondeur lorsque le personnage va vers le haut
//    protected boolean ignoreZ = false;
//
//    // Mouvement du personnage
//    protected boolean mouvementX, mouvementY;
//    protected Signal<Boolean> onMouvementChange;
//
//    protected float currentSpeed, maxSpeed, acceleration;
//
//    public Personnage ignoreZ() {
//        ignoreZ = true; return this;
//    }
//
//    public Personnage walkTo(final Direction newDirection) {
//        final Direction oldDir = lastDirection;
//        final boolean oldMouvement = mouvementX || mouvementY;
//
//        if (newDirection.vertical() && canMoveY()) {
//            mouvementY = true;
//            lastDirection = directionY = newDirection;
//        } else if (!newDirection.vertical() && canMoveX()) {
//            mouvementX = true;
//            lastDirection = directionX = newDirection;
//        }
//        if (onDirectionChange != null && oldDir != lastDirection)
//            onDirectionChange.emit(lastDirection);
//        if (onMouvementChange != null && oldMouvement != (mouvementX || mouvementY))
//            onMouvementChange.emit(mouvementX || mouvementY);
//
//        return this;
//    }
//    public Personnage stopWalk() {
//        stopWalk(true); return stopWalk(false);
//    }
//    public Personnage stopWalk(final boolean vertical) {
//        if (vertical) mouvementY = false;
//        else mouvementX = false;
//        return this;
//    }
//
//    /**
//     * Animations
//     */
//    public Personnage initLeft(final AnimationTemplate anim, final AnimationTemplate idle) {
//        addAnim(D_LEFT, anim);
//        addAnim(D_IDLE + D_LEFT, idle);
//        if (!animations.containsKey(D_RIGHT)) {
//            cloneAnim(D_LEFT, D_RIGHT, true, false);
//            cloneAnim(D_IDLE + D_LEFT, D_IDLE + D_RIGHT, true, false);
//        }
//        return this;
//    }
//
//    public Personnage initRight(final AnimationTemplate anim, final AnimationTemplate idle) {
//        addAnim(D_RIGHT, anim);
//        addAnim(D_IDLE + D_RIGHT, idle);
//        if (!animations.containsKey(D_LEFT)) {
//            cloneAnim(D_RIGHT, D_LEFT, true, false);
//            cloneAnim(D_IDLE + D_RIGHT, D_IDLE + D_LEFT, true, false);
//        }
//        return this;
//    }
//
//    public Personnage initUp(final AnimationTemplate anim, final AnimationTemplate idle) {
//        addAnim(D_UP, anim);
//        addAnim(D_IDLE + D_UP, idle);
//        if (!animations.containsKey(D_DOWN)) {
//            cloneAnim(D_UP, D_DOWN, false, true);
//            cloneAnim(D_IDLE + D_UP, D_IDLE + D_DOWN, false, true);
//        }
//        return this;
//    }
//
//    public Personnage initDown(final AnimationTemplate anim, final AnimationTemplate idle) {
//        addAnim(D_DOWN, anim);
//        addAnim(D_IDLE + D_DOWN, idle);
//        if (!animations.containsKey(D_UP)) {
//            cloneAnim(D_DOWN, D_UP, false, true);
//            cloneAnim(D_IDLE + D_DOWN, D_IDLE + D_UP, false, true);
//        }
//        return this;
//    }
//
//    public boolean canMoveY() {
//        return animations.containsKey(D_UP) || animations.containsKey(D_DOWN);
//    }
//
//    public boolean canMoveX() {
//        return animations.containsKey(D_LEFT) || animations.containsKey(D_RIGHT);
//    }
//
//    public boolean canMoveXY() {
//        return animations.containsKey(D_UP + D_LEFT) || animations.containsKey(D_UP + D_RIGHT) ||
//                animations.containsKey(D_DOWN + D_LEFT) || animations.containsKey(D_DOWN + D_RIGHT);
//    }
//
//    /**
//     * Eventable
//     */
//    public Personnage onMouvementChange(final Signal.Listener<Boolean> slot) {
//        if (onMouvementChange == null) onMouvementChange = Signal.create();
//        onMouvementChange.connect(slot);
//        return this;
//    }
//    public Personnage onDirectionChange(final Signal.Listener<Direction> slot) {
//        if (onDirectionChange == null) onDirectionChange= Signal.create();
//        onDirectionChange.connect(slot);
//        return this;
//    }
//
//    public Personnage activeControleClavier(final BebelProcessor input) {
//        input.onKeyDown(key -> {
//            final Direction newDirection = Direction.byKey(key.key);
//            walkTo(newDirection);
//        });
//        input.onKeyUp(key -> {
//            final Direction dir = Direction.byKey(key.key);
//            if (dir == directionX) stopWalk(false);
//            else if (dir == directionY) stopWalk(true);
//        });
//        return this;
//    }
//
//    public Personnage activeControleSouris(final GroupElement map) {
//        map.onClick(mouse -> {
//            stop();
//            newParallel(walk(this).centerTo(mouse.x, mouse.y));
//        });
//        return this;
//    }
//}
