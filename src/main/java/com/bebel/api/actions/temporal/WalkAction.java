package com.bebel.api.actions.temporal;

/**
 * Action de deplacement d'un personnage
 */
//public class WalkAction extends BindAction<Personnage> {
//    protected Point startPoint = new Point(), endPoint = new Point(), objectif = new Point();
//    protected Point sens = new Point();
//
//    @Override
//    protected void begin() {
//        super.begin();
//        startPoint.set(target.x(), target.y());
//        if (isBy) objectif.set(target.x() + endPoint.x, target.y() + endPoint.y);
//        else objectif.set(endPoint);
//        sens.set(objectif.x - startPoint.x, objectif.y - startPoint.y);
//    }
//
//    @Override
//    protected boolean act(float delta) {
//        boolean xFinish = false, yFinish = false;
//
//        if (target.x() < objectif.x && sens.x > 0) target.walkTo(Direction.RIGHT);
//        else if (target.x() > objectif.x && sens.x < 0) target.walkTo(Direction.LEFT);
//        else {
//            xFinish = true; target.stopWalk(false);
//        }
//
//        if (target.y() < objectif.y && sens.y > 0) target.walkTo(Direction.UP);
//        else if (target.y() > objectif.y && sens.y < 0) target.walkTo(Direction.DOWN);
//        else {
//            yFinish = true; target.stopWalk(true);
//        }
//
//        return xFinish && yFinish;
//    }
//
//    @Override
//    protected void actTime(float percent) {}
//
//    public WalkAction to(final float ox, final float oy, final int from) {
//        float x = ox;
//        float y = oy;
//
//        if ((from & R_UP) != 0) {
//            y = target.parentHeight() - target.height() - y;
//        }
//        if ((from & R_RIGHT) != 0) {
//            x = target.parentWidth() - target.width() - x;
//        }
//
//        isBy = false;
//        endPoint.set(x, y);
//        return this;
//    }
//    public WalkAction to(final float x, final float y) {
//        return to(x, y, R_UP | R_LEFT);
//    }
//    public WalkAction bottomTo(final float x, final float y) {
//        return to(x, y - target.height());
//    }
//    public WalkAction bottomCenterTo(final float x, final float y) {
//        return to(x - target.width()/2, y - target.height());
//    }
//    public WalkAction centerTo(final float x, final float y) {
//        return to(x - target.width()/2, y - target.height()/2);
//    }
//
//    public WalkAction by(final float ox, final float oy, final int from) {
//        float x = ox, y = oy;
//        if ((from & R_UP) != 0) y = -y;
//        if ((from & R_RIGHT) != 0) x = -x;
//
//        isBy = true;
//        endPoint.set(x, y);
//        return this;
//    }
//
//    public WalkAction by(final float x, final float y) {
//        return by(x, y, R_UP | R_LEFT);
//    }
//
//    @Override
//    public void pause() {
//        super.pause(); target.stopWalk();
//    }
//
//    @Override
//    public void reset() {
//        super.reset();
//        isBy = false; sens.set(0, 0);
//        startPoint.set(-1, -1); endPoint.set(startPoint);
//    }
//
//    @Override
//    public String type() {
//        return "Move";
//    }
//}
