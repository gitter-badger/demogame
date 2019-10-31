package core.collision;

import core.Game;
import core.map.MapLevel;
import core.map.MapObject;
import core.object.GameObject;
import core.system.AbstractSystem;
import core.system.System;

import java.util.HashMap;
import java.util.Map;

import static core.collision.CollisionEvent.CollisionType.COLLISION_MAP;

/**
 * The MapColliding service is dedicated to check GameObject vs. MapObject from the map tiles.
 *
 * @author Frédéric Delorme<frederic.delorme@gmail.com>
 * @since 2019
 */
public class MapCollidingService extends AbstractSystem implements System {


    Map<Class<?>, OnCollision> listeners = new HashMap<>();

    public MapCollidingService(Game g) {
        super(g);
    }

    @Override
    public String getName() {
        return MapCollidingService.class.getCanonicalName();
    }

    @Override
    public int initialize(Game game) {
        return 0;
    }

    @Override
    public void dispose() {

    }

    public void addListener(Class<?> clazz, OnCollision oc) {
        if (!listeners.containsKey(clazz)) {
            listeners.put(clazz, oc);
        }
    }

    /**
     * Check the collision between the MapLevel tiles and a GameObject
     *
     * @param map the map to check against
     * @param go  the GameObject to be verified.
     */
    public void checkCollision(MapLevel map, GameObject go) {
        int ox = (int) (go.bbox.x / map.asset.tileWidth);
        int ow = (int) (go.bbox.width / map.asset.tileWidth);
        int oy = (int) (go.bbox.y / map.asset.tileHeight);
        int oh = (int) (go.bbox.height / map.asset.tileHeight);

        MapObject mo;

        if (go.dx > 0) {
            boolean blocking = false;
            for (int iy = oy; iy < oy + oh; iy++) {
                mo = map.tiles[ox + ow][iy];
                mo = getTileInMap(map, ox + ow, iy);
                if (mo != null) {
                    if (mo.block) {
                        go.dx = 0.0f;
                        go.x = go.oldX;
                        break;
                    } else
                        collide(go, map, mo, ox + ow, iy);
                }
            }
        }

        if (go.dx < 0) {
            boolean blocking = false;
            for (int iy = oy; iy < oy + oh; iy++) {
                mo = getTileInMap(map, ox, iy);
                if (mo != null) {

                    if (mo.block) {
                        go.dx = 0.0f;
                        go.x = go.oldX;
                        break;
                    } else
                        collide(go, map, mo, ox, iy);
                }
            }
            if (go.dy > 0) {

            }
            if (go.dy < 0) {

            }
        }
    }

    /**
     * As the `MapObject` is not null and is not a blocking one, we try to collect it,
     * and test if the `MapObject` type is an item or an object.
     *
     * @param go  the `GameObject` that `canCollect`
     * @param map the map where to search for
     * @param mo  the MapObject to be tested with
     * @param x   the horizontal position in the tiles map
     * @param y   the vertical position in the tiles map
     */
    private void collide(GameObject go, MapLevel map, MapObject mo, int x, int y) {
        listeners.get(go.getClass()).collide(new CollisionEvent(COLLISION_MAP, go, null, mo, map, x, y));
    }

    /**
     * Retrieve the MapObject from the tiles map. it's also checking that (x,y) is not out pf the map.
     * return null elsewhere.
     *
     * @param map the map to find
     * @param x   the horizontal position to test
     * @param y   the vertical position to test
     * @return
     */
    private MapObject getTileInMap(MapLevel map, int x, int y) {
        if (x < 0 || y < 0 || x > map.tiles.length || y > map.tiles[0].length) {
            return null;
        }
        return map.tiles[x][y];
    }
}
