package ds.plato.item.spell;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.vecmath.Point3i;

import com.google.gson.Gson;

import ds.plato.select.Selection;

public class PersistentVoxelGroup {

	public Point3i insertionPoint;
	public Voxel[] voxels;

	public PersistentVoxelGroup(Point3i insertionPoint, List<Selection> selections) {
		this.insertionPoint = insertionPoint;
		Voxel[] voxels = new Voxel[selections.size()];
		for (int i = 0; i < voxels.length; i++) {
			voxels[i] = new Voxel(selections.get(i));
		}
		this.voxels = voxels;
	}

	@Override
	public String toString() {
		return "Group [insertionPoint=" + insertionPoint + ", voxels=" + Arrays.toString(voxels) + "]";
	}

	public class Voxel {
		public String b;
		public int x, y, z, m;

		public Voxel(Selection s) {
			//FIXME broken for Quartz
			b = s.block.getUnlocalizedName().substring(5);
			// b = s.block.toString();
	 		x = s.x;
			y = s.y;
			z = s.z;
			m = s.metadata;
		}

		@Override
		public String toString() {
			return "Voxel [b=" + b + ", x=" + x + ", y=" + y + ", z=" + z + ", m=" + m + "]";
		}
	}

	public String write(String fileName) throws IOException {
		String json = new Gson().toJson(this);
		FileWriter writer = new FileWriter(fileName);
		writer.write(json);
		writer.close();
		return json;
	}

	public static PersistentVoxelGroup read(String fileName) throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		// Type listType = new TypeToken<ArrayList<Voxel>>() {
		// }.getType();
		PersistentVoxelGroup g = new Gson().fromJson(br, PersistentVoxelGroup.class);
		return g;
	}
}
