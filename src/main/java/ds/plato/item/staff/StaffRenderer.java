package ds.plato.item.staff;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import ds.plato.item.IItem;
import ds.plato.item.spell.Spell;

//Based on http://greyminecraftcoder.blogspot.com.au/2013/09/custom-item-rendering-using.html
public class StaffRenderer implements IItemRenderer {

	//private IModelCustom staffModel;
	private ResourceLocation modelResourceLocation;
	private ResourceLocation textureResourceLocation;

	private enum TransformationTypes {
		NONE,
		DROPPED,
		INVENTORY,
		THIRDPERSONEQUIPPED
	};

	public StaffRenderer(IItem staff) {
		//staffModel = staff.getModel();
		modelResourceLocation = staff.getModelResourceLocation();
		textureResourceLocation = staff.getTextureResourceLocation();
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
		//1.8
		//EntityClientPlayerMP p = Minecraft.getMinecraft().thePlayer;
		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		if (!p.isSprinting()) {
			TransformationTypes transformationToBeUndone = doTransform(type);
			renderStaff();
			renderSpell(stack);
			undoTransform(transformationToBeUndone);
		}
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch (type) {
		case ENTITY:
		case EQUIPPED:
		case EQUIPPED_FIRST_PERSON:
		case INVENTORY:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		switch (type) {
		case ENTITY:
			return (helper == ItemRendererHelper.ENTITY_BOBBING || helper == ItemRendererHelper.ENTITY_ROTATION || helper == ItemRendererHelper.BLOCK_3D);
		case EQUIPPED:
			return (helper == ItemRendererHelper.BLOCK_3D || helper == ItemRendererHelper.EQUIPPED_BLOCK);
		case EQUIPPED_FIRST_PERSON:
			return (helper == ItemRendererHelper.EQUIPPED_BLOCK);
		case INVENTORY:
			return (helper == ItemRendererHelper.INVENTORY_BLOCK);
		default:
			return false;
		}
	}

	private TransformationTypes doTransform(ItemRenderType type) {
		// adjust rendering space to match what caller expects
		TransformationTypes transformationToBeUndone = TransformationTypes.NONE;
		switch (type) {
		case EQUIPPED: { // backface culling is off so we need to turn it back on to render the lampshade correctly,
							// since our faces are single-sided.
							// Normally this is not necessary even for transparent
							// objects, unless you don't want to see the inside (back face) of the opposite side of the
							// cube when you look through the cube
			GL11.glEnable(GL11.GL_CULL_FACE);
			transformationToBeUndone = TransformationTypes.THIRDPERSONEQUIPPED;
			break;
		}
		case EQUIPPED_FIRST_PERSON: {
			break; // caller expects us to render over [0,0,0] to [1,1,1], no transformation necessary
		}
		case INVENTORY: { // caller expects [-0.5, -0.5, -0.5] to [0.5, 0.5, 0.5]
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			transformationToBeUndone = TransformationTypes.INVENTORY;
			break;
		}
		case ENTITY: {
			// translate our coordinates and scale so that [0,0,0] to [1,1,1] translates to the [-0.25, -0.25, -0.25] to
			// [0.25, 0.25, 0.25] expected by the caller.
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			transformationToBeUndone = TransformationTypes.DROPPED;
			break;
		}
		default:
			break; // never here
		}
		return transformationToBeUndone;
	}

	private void undoTransform(TransformationTypes transformationToBeUndone) {
		switch (transformationToBeUndone) {
		case NONE: {
			break;
		}
		case DROPPED: {
			GL11.glTranslatef(0.5F, 0.5F, 0.0F);
			GL11.glScalef(2.0F, 2.0F, 2.0F);
			break;
		}
		case INVENTORY: {
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			break;
		}
		case THIRDPERSONEQUIPPED: {
			GL11.glDisable(GL11.GL_CULL_FACE);
		}
		default:
			break;
		}
	}
	
	private void renderStaff() {
		//1.8
//		GL11.glPushMatrix();
//		GL11.glTranslated(0, 0, .5);
//		GL11.glRotated(-15, 0, 0, 1);
//		Minecraft.getMinecraft().renderEngine.bindTexture(textureResourceLocation);
//		staffModel.renderAll();
//		GL11.glPopMatrix();
	}

	private void renderSpell(ItemStack stack) {
		Staff staff = (Staff) stack.getItem();
		if (staff != null) {
			Spell spell = staff.getSpell(stack);
			if (spell != null) {
				//1.8
//				IModelCustom spellModel = spell.getModel();
//				if (spellModel != null) {
//					GL11.glPushMatrix();
//					GL11.glTranslated(0, 1.5, 0);
//					GL11.glScaled(.6, .6, .6);
//					ResourceLocation spellTexture = spell.getTextureResourceLocation();
//					Minecraft.getMinecraft().renderEngine.bindTexture(spellTexture);
//					spellModel.renderAll();
//					GL11.glPopMatrix();
//				}
			}
		}
	}
}