package alchemyplusplus.network.message;

import alchemyplusplus.block.complex.diffuser.DiffuserTileEntity;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;

public class DiffuserUpdateMessage implements IMessage, IMessageHandler<DiffuserUpdateMessage, IMessage>
{

    private int posX, posY, posZ;
    private int bottleColor, potionDamageValue, fluidLevel, fluidID;
    private boolean isDiffusing;
    private int[] effectIDs;

    public DiffuserUpdateMessage()
    {

    }

    public DiffuserUpdateMessage(DiffuserTileEntity diffuser)
    {
        this.posX = diffuser.xCoord;
        this.posY = diffuser.yCoord;
        this.posZ = diffuser.zCoord;

        this.bottleColor = diffuser.bottleColor;
        this.potionDamageValue = diffuser.potionDamageValue;
        this.isDiffusing = diffuser.isDiffusing;
        this.fluidLevel = diffuser.getFluidAmount();
        if (diffuser.getFluid() != null)
        {
            this.fluidID = diffuser.getFluid().fluidID;
        } else
        {
            this.fluidID = 0;
        }

        this.effectIDs = new int[diffuser.fluidTank.potionEffects.size()];
        int id = 0;

        for (PotionEffect potionEffect : diffuser.fluidTank.potionEffects)
            this.effectIDs[id++] = potionEffect.getPotionID();

    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.posX = buf.readInt();
        this.posY = buf.readInt();
        this.posZ = buf.readInt();

        this.bottleColor = buf.readInt();
        this.potionDamageValue = buf.readInt();
        this.isDiffusing = buf.readBoolean();
        this.fluidLevel = buf.readInt();
        this.fluidID = buf.readInt();

        int size = buf.readInt();
        this.effectIDs = new int[size];
        int id = 0;
        while (id < size)
            this.effectIDs[id++] = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.posX);
        buf.writeInt(this.posY);
        buf.writeInt(this.posZ);

        buf.writeInt(this.bottleColor);
        buf.writeInt(this.potionDamageValue);
        buf.writeBoolean(this.isDiffusing);
        buf.writeInt(this.fluidLevel);
        buf.writeInt(this.fluidID);

        buf.writeInt(this.effectIDs.length);
        for (int id : this.effectIDs)
            buf.writeInt(id);
    }

    @Override
    public IMessage onMessage(DiffuserUpdateMessage message, MessageContext ctx)
    {
        TileEntity tile = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.posX, message.posY, message.posZ);
        if (tile instanceof DiffuserTileEntity)
        {
            ((DiffuserTileEntity) tile).setBottleColorValue(message.bottleColor);
            ((DiffuserTileEntity) tile).setDiffusingState(message.isDiffusing);
            ((DiffuserTileEntity) tile).syncFluidAmountAt(message.fluidLevel, message.fluidID);
            ((DiffuserTileEntity) tile).potionDamageValue = this.potionDamageValue;
        }
        return null;
    }
}
