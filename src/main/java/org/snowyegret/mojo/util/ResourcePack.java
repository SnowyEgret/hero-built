package org.snowyegret.mojo.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

import org.snowyegret.mojo.MoJo;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ResourcePack implements IResourcePack {

	Map<ResourceLocation, String> map = Maps.newHashMap();

	public ResourcePack(Class cls, String json) {
		ModelResourceLocation mrl = ModelResourceLocations.forClass(cls);
		System.out.println("mrl=" + mrl);
		map.put(mrl, json);
	}

	@Override
	public InputStream getInputStream(ResourceLocation loc) throws IOException {
		String json = map.get(loc);
		return new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public boolean resourceExists(ResourceLocation loc) {
		return map.containsKey(loc);
	}

	@Override
	public Set getResourceDomains() {
		return Sets.newHashSet(MoJo.DOMAIN);
	}

	@Override
	public IMetadataSection getPackMetadata(IMetadataSerializer p_135058_1_, String p_135058_2_) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BufferedImage getPackImage() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPackName() {
		return MoJo.NAME;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResourcePack [map=");
		builder.append(map.keySet());
		builder.append(", getResourceDomains()=");
		builder.append(getResourceDomains());
		builder.append(", getPackName()=");
		builder.append(getPackName());
		builder.append("]");
		return builder.toString();
	}

}
